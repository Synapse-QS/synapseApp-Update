package com.synapse.social.studioasinc.domain.usecase.post

import com.synapse.social.studioasinc.data.repository.PostRepository
import javax.inject.Inject

class RepostPostUseCase @Inject constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(postId: String): Result<Unit> {
        return postRepository.resharePost(postId)
    }
}
