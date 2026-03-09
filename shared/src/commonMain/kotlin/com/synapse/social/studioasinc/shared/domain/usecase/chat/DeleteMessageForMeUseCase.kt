package com.synapse.social.studioasinc.shared.domain.usecase.chat

import com.synapse.social.studioasinc.shared.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteMessageForMeUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(messageId: String): Result<Unit> = repository.deleteMessageForMe(messageId)
}
