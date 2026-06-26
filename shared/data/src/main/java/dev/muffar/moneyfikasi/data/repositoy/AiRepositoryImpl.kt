package dev.muffar.moneyfikasi.data.repositoy

import dev.muffar.moneyfikasi.data.BuildConfig
import dev.muffar.moneyfikasi.data.mapper.toAiError
import dev.muffar.moneyfikasi.data.remote.groq.GroqApiService
import dev.muffar.moneyfikasi.data.remote.groq.GroqChatRequest
import dev.muffar.moneyfikasi.data.remote.groq.GroqMessage
import dev.muffar.moneyfikasi.domain.model.AiError
import dev.muffar.moneyfikasi.domain.model.AiException
import dev.muffar.moneyfikasi.domain.model.AiTransactionResult
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.AiRepository
import dev.muffar.moneyfikasi.domain.repository.CategoryRepository
import dev.muffar.moneyfikasi.domain.repository.WalletRepository
import dev.muffar.moneyfikasi.utils.extensions.StringExt.cleanJson
import dev.muffar.moneyfikasi.utils.extensions.StringExt.normalizeAmountText
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val groqApi: GroqApiService,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository
) : AiRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun parseTransaction(input: String): Result<AiTransactionResult> {
        val categories = runCatching {
            categoryRepository.getAllCategories(false)
                .first()
                .filter { it.isActive }
                .map { it.name }
        }.getOrDefault(emptyList())

        val wallets = runCatching {
            walletRepository.getAllWallets()
                .first()
                .filter { it.isActive }
                .map { it.name }
        }.getOrDefault(emptyList())

        val prompt = """
                 Parse transaction text to JSON.

                Categories: $${categories.joinToString("|")}
                Wallets: $${wallets.joinToString("|")}
            
                Return only one valid JSON.
            
                Schema:
                - EXPENSE/INCOME: {"amount":0,"note":"","type":"EXPENSE|INCOME","category":null,"wallet":null}
                - TRANSFER: {"amount":0,"note":"","fee":0,"type":"TRANSFER","from_wallet":null,"to_wallet":null}
            
                Rules:
                - Match wallet/category from lists even with typo, abbreviation, or different case.
                - Output wallet/category using the exact name from the lists.
                - Use null if unclear.
                - If text contains two wallets, treat it as TRANSFER.
                - For pattern "amount A ke B", use A as from_wallet and B as to_wallet.
                - If TRANSFER, search for admin fee or transaction fee and put it in "fee" field.
                - If TRANSFER, do not return category or wallet.
                - Return JSON only.
            
                Text: $${input.normalizeAmountText()}
            """.trimIndent()

        return try {
            val response = groqApi.getChatCompletion(
                apiKey = "Bearer ${BuildConfig.GROQ_API_KEY}",
                request = GroqChatRequest(
                    model = "llama-3.3-70b-versatile",
                    messages = listOf(
                        GroqMessage(
                            role = "user",
                            content = prompt
                        )
                    )
                )
            )

            val jsonString = response.choices.firstOrNull()?.message?.content?.cleanJson()
                ?: throw AiException(AiError.EmptyResponse)

            val result = try {
                json.decodeFromString<AiTransactionResultJson>(jsonString)
            } catch (e: Exception) {
                throw AiException(AiError.InvalidJson, e)
            }

            Result.success(
                AiTransactionResult(
                    amount = result.amount,
                    note = result.note,
                    type = result.type.toTransactionType(),
                    category = result.category,
                    wallet = result.wallet,
                    fromWallet = result.fromWallet,
                    toWallet = result.toWallet,
                    fee = result.fee
                )
            )
        } catch (e: Throwable) {
            Result.failure(AiException(e.toAiError(), e))
        }
    }

    private fun String.toTransactionType(): TransactionType {
        return when (this.uppercase()) {
            "INCOME" -> TransactionType.INCOME
            "TRANSFER" -> TransactionType.TRANSFER_OUT
            else -> TransactionType.EXPENSE
        }
    }
}

@kotlinx.serialization.Serializable
private data class AiTransactionResultJson(
    val amount: Double,
    val note: String,
    val type: String,
    val fee: Double? = null,
    val category: String? = null,
    val wallet: String? = null,
    @SerialName("from_wallet") val fromWallet: String? = null,
    @SerialName("to_wallet") val toWallet: String? = null
)
