package com.synapse.social.studioasinc.shared.domain.repository

import kotlinx.coroutines.flow.Flow

interface PresenceRepository {
    suspend fun updatePresence(isOnline: Boolean): Result<Unit>
    suspend fun startPresenceTracking()
    suspend fun stopPresenceTracking()
    fun observeUserPresence(userId: String): Flow<Boolean>
}
