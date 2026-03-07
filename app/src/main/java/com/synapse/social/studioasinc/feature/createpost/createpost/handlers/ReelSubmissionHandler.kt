package com.synapse.social.studioasinc.feature.createpost.createpost.handlers

import android.app.Application
import com.synapse.social.studioasinc.domain.model.FeelingActivity
import com.synapse.social.studioasinc.domain.model.LocationData
import com.synapse.social.studioasinc.domain.model.MediaItem
import com.synapse.social.studioasinc.domain.model.User
import com.synapse.social.studioasinc.shared.data.repository.ReelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReelSubmissionHandler @Inject constructor(
    private val application: Application,
    private val reelRepository: ReelRepository,
    private val uploadMediaUseCase: com.synapse.social.studioasinc.shared.domain.usecase.UploadMediaUseCase
) {

    suspend fun submitReel(
        videoItem: MediaItem,
        postText: String,
        location: LocationData?,
        taggedPeople: List<User>,
        feeling: FeelingActivity?,
        textBackgroundColor: Long?,
        onProgress: (Float) -> Unit
    ): Result<Unit> {
        val metadataMap = mutableMapOf<String, Any?>()
        feeling?.let { metadataMap["feeling"] = mapOf("emoji" to it.emoji, "text" to it.text, "type" to it.type.name) }
        if (taggedPeople.isNotEmpty()) {
            metadataMap["tagged_people"] = taggedPeople.map { mapOf("uid" to it.uid, "username" to it.username) }
        }
        metadataMap["layout_type"] = DEFAULT_LAYOUT_TYPE
        textBackgroundColor?.let { metadataMap["background_color"] = it }

        return withContext(Dispatchers.IO) {
            try {
                // Upload video using the configured storage provider
                val videoUrl = uploadMediaUseCase(
                    filePath = videoItem.url,
                    mediaType = com.synapse.social.studioasinc.shared.domain.model.MediaType.VIDEO,
                    bucketName = "reels",
                    onProgress = onProgress
                ).getOrThrow()

                reelRepository.createReel(
                    videoUrl = videoUrl,
                    caption = postText,
                    musicTrack = "Original Audio",
                    locationName = location?.name,
                    locationAddress = location?.address,
                    locationLatitude = location?.latitude,
                    locationLongitude = location?.longitude,
                    metadata = metadataMap
                )
            } catch (e: Exception) {
                android.util.Log.e("ReelSubmissionHandler", "Reel submission failed: ${e.message}", e)
                Result.failure(Exception("Reel submission failed: ${e.message ?: e::class.simpleName}", e))
            }
        }
    }

    companion object {
        private const val DEFAULT_LAYOUT_TYPE = "COLUMNS"
    }
}

