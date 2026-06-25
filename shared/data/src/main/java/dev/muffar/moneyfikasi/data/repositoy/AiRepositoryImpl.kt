package dev.muffar.moneyfikasi.data.repositoy

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.RequestOptions
import dev.muffar.moneyfikasi.data.BuildConfig
import dev.muffar.moneyfikasi.data.mapper.toAiError
import dev.muffar.moneyfikasi.domain.model.AiError
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

    private val json = Json { 
        ignoreUnknownKeys = true 
        isLenient = true
    }

    override suspend fun parseTransaction(input: String): Result<AiTransactionResult> {
        val prompt = """
            You are a personal finance assistant. Extract transaction details from the following text and return it in JSON format.
            The JSON should have these keys: "amount" (number), "note" (string), "type" (string, either "EXPENSE" or "INCOME").
            Text: "$input"
            JSON:
        """.trimIndent()

        return try {
            val response = model.generateContent(prompt)
            val jsonString = response.text?.cleanJson() 
                ?: throw AiError.EmptyResponse
            
            val result = try {
                json.decodeFromString<AiTransactionResultJson>(jsonString)
            } catch (e: Exception) {
                throw AiError.InvalidJson
            }

            Result.success(
                AiTransactionResult(
                    amount = result.amount,
                    note = result.note,
                    type = result.type.toTransactionType()
                )
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(e.toAiError())
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
    val type: String
)
