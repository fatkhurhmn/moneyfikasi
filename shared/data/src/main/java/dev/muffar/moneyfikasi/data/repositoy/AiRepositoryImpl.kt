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
import kotlinx.coroutines.flow.first
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
        val categories = try {
            categoryRepository.getAllCategories(false)
                .first()
                .filter { it.isActive }
                .map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
        val wallets = try {
            walletRepository.getAllWallets()
                .first()
                .filter { it.isActive }
                .map { it.name }
        } catch (e: Exception) {
            emptyList()
        }

        val prompt = """
            Parse this transaction.
            Categories: ${categories.joinToString(",")}
            Wallets: ${wallets.joinToString(",")}
            
            Return only:
            {"amount":0,"note":"","type":"EXPENSE|INCOME","category":null,"wallet":null}
            
            category/wallet must exactly match the lists or be null.
            
            Text: $input
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
                    wallet = result.wallet
                )
            )
        } catch (e: Throwable) {
            Result.failure(AiException(e.toAiError(), e))
        }
    }

    private fun String.cleanJson(): String {
        val start = this.indexOf("{")
        val end = this.lastIndexOf("}")
        return if (start != -1 && end != -1 && end > start) {
            this.substring(start, end + 1)
        } else {
            this.replace("```json", "")
                .replace("```", "")
                .trim()
        }
    }

    private fun String.toTransactionType(): TransactionType {
        return try {
            TransactionType.valueOf(this.uppercase())
        } catch (e: Exception) {
            TransactionType.EXPENSE
        }
    }
}

@kotlinx.serialization.Serializable
private data class AiTransactionResultJson(
    val amount: Double,
    val note: String,
    val type: String,
    val category: String? = null,
    val wallet: String? = null
)
