package dev.muffar.moneyfikasi.data.repositoy

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.RequestOptions
import dev.muffar.moneyfikasi.data.BuildConfig
import dev.muffar.moneyfikasi.domain.model.AiTransactionResult
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.repository.AiRepository
import kotlinx.serialization.json.Json

class AiRepositoryImpl : AiRepository {
    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        requestOptions = RequestOptions(apiVersion = "v1")
    )

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun parseTransaction(input: String): AiTransactionResult? {
        val prompt = """
            You are a personal finance assistant. Extract transaction details from the following text and return it in JSON format.
            The JSON should have these keys: "amount" (number), "note" (string), "type" (string, either "EXPENSE" or "INCOME").
            Text: "$input"
            JSON:
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val jsonString =
                response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: return null
            val result = json.decodeFromString<AiTransactionResultJson>(jsonString)
            AiTransactionResult(
                amount = result.amount,
                note = result.note,
                type = try {
                    TransactionType.valueOf(result.type.uppercase())
                } catch (e: Exception) {
                    TransactionType.EXPENSE
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@kotlinx.serialization.Serializable
private data class AiTransactionResultJson(
    val amount: Double,
    val note: String,
    val type: String
)
