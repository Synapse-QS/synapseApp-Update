package com.synapse.social.studioasinc.domain.repository.ai

interface AiRepository {
    suspend fun generateSmartReplies(recentMessages: List<String>): Result<List<String>>
    suspend fun summarizeChat(messages: List<String>): Result<String>
}
