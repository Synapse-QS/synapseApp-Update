package com.synapse.social.studioasinc.data.repository.ai

import com.google.genai.Client
import com.synapse.social.studioasinc.domain.repository.ai.AiRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiRepositoryImpl(private val apiKey: String) : AiRepository {

    private val client: Client? by lazy {
        if (apiKey.isNotBlank()) {
            Client.builder().apiKey(apiKey).build()
        } else {
            null
        }
    }

    override suspend fun generateSmartReplies(recentMessages: List<String>): Result<List<String>> = withContext(Dispatchers.IO) {
        if (client == null) {
            return@withContext Result.failure(IllegalStateException("Gemini API key is not configured."))
        }

        if (recentMessages.isEmpty()) {
            return@withContext Result.success(emptyList())
        }

        try {
            val prompt = """
                Based on the following recent chat messages, suggest 3 short, context-aware smart replies.
                Provide only the replies, each on a new line, without any numbering or extra text.

                Recent messages:
                ${recentMessages.joinToString("\n")}
            """.trimIndent()

            val response = client!!.models.generateContent(
                "gemini-1.5-flash",
                prompt,
                null
            )
            val text = response.text()

            if (text != null) {
                val replies = text.lines()
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .take(3)
                Result.success(replies)
            } else {
                Result.failure(Exception("Failed to generate smart replies: Empty response from model."))
            }
        } catch (e: Exception) {
            Napier.e("Error generating smart replies", e)
            Result.failure(e)
        }
    }

    override suspend fun summarizeChat(messages: List<String>): Result<String> = withContext(Dispatchers.IO) {
        if (client == null) {
            return@withContext Result.failure(IllegalStateException("Gemini API key is not configured."))
        }

        if (messages.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Cannot summarize an empty chat."))
        }

        try {
            val prompt = """
                Summarize the following chat conversation concisely in a few sentences.
                Highlight the main topics discussed and any key decisions or action items.

                Conversation:
                ${messages.joinToString("\n")}
            """.trimIndent()

            val response = client!!.models.generateContent(
                "gemini-1.5-flash",
                prompt,
                null
            )
            val text = response.text()

            if (text != null) {
                Result.success(text.trim())
            } else {
                Result.failure(Exception("Failed to summarize chat: Empty response from model."))
            }
        } catch (e: Exception) {
            Napier.e("Error summarizing chat", e)
            Result.failure(e)
        }
    }
}
