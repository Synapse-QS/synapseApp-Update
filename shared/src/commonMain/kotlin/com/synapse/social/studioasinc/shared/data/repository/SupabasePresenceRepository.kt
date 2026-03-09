package com.synapse.social.studioasinc.shared.data.repository

import com.synapse.social.studioasinc.shared.core.network.SupabaseClient
import com.synapse.social.studioasinc.shared.domain.repository.PresenceRepository
import io.github.aakira.napier.Napier
import io.github.jan.supabase.SupabaseClient as SupabaseClientLib
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabasePresenceRepository(
    private val client: SupabaseClientLib = SupabaseClient.client
) : PresenceRepository {
    
    private var heartbeatJob: Job? = null
    private val presenceChannel by lazy { client.realtime.channel("presence") }
    
    override suspend fun updatePresence(isOnline: Boolean): Result<Unit> = runCatching {
        val userId = client.auth.currentUserOrNull()?.id ?: return Result.failure(Exception("Not authenticated"))
        
        withContext(Dispatchers.IO) {
            client.postgrest.from("user_presence").upsert(
                buildJsonObject {
                    put("user_id", userId)
                    put("is_online", isOnline)
                    put("last_seen", Clock.System.now().toString())
                }
            )
        }
    }
    
    override suspend fun startPresenceTracking() {
        val userId = client.auth.currentUserOrNull()?.id ?: return
        
        presenceChannel.subscribe(blockUntilSubscribed = true)
        presenceChannel.track(buildJsonObject {
            put("user_id", userId)
            put("online", true)
        })
        
        heartbeatJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                updatePresence(true)
                delay(30_000) // 30s heartbeat
            }
        }
    }
    
    override suspend fun stopPresenceTracking() {
        heartbeatJob?.cancel()
        updatePresence(false)
        presenceChannel.unsubscribe()
    }
    
    override fun observeUserPresence(userId: String): Flow<Boolean> {
        // TODO: Implement with Supabase 3.x Realtime Presence API
        // For now, poll the database
        return kotlinx.coroutines.flow.flow {
            while (true) {
                val isOnline = runCatching {
                    val response = client.postgrest.from("user_presence")
                        .select {
                            filter {
                                eq("user_id", userId)
                                eq("is_online", true)
                            }
                        }
                    response.data.isNotEmpty()
                }.getOrDefault(false)
                emit(isOnline)
                delay(5000) // Poll every 5 seconds
            }
        }
    }
}
