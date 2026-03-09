package com.synapse.social.studioasinc.shared.data.repository

import com.synapse.social.studioasinc.shared.core.network.SupabaseClient
import com.synapse.social.studioasinc.shared.data.datasource.SupabaseChatDataSource
import com.synapse.social.studioasinc.shared.data.mapper.ChatMapper.toDomain
import com.synapse.social.studioasinc.shared.domain.model.chat.Conversation
import com.synapse.social.studioasinc.shared.domain.model.chat.Message
import com.synapse.social.studioasinc.shared.domain.model.chat.TypingStatus
import com.synapse.social.studioasinc.shared.domain.repository.ChatRepository
import io.github.aakira.napier.Napier
import io.github.jan.supabase.SupabaseClient as SupabaseClientLib
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.async
import com.synapse.social.studioasinc.shared.data.crypto.SignalProtocolManager
import com.synapse.social.studioasinc.shared.data.crypto.models.EncryptedMessage
import com.synapse.social.studioasinc.shared.data.crypto.models.PreKeyBundle
import com.synapse.social.studioasinc.shared.data.dto.chat.MessageDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SupabaseChatRepository(
    private val dataSource: SupabaseChatDataSource = SupabaseChatDataSource(),
    private val client: SupabaseClientLib = SupabaseClient.client,
    private val signalProtocolManager: SignalProtocolManager? = null
) : ChatRepository {

    private suspend fun MessageDto.decryptIfNecessary(currentUserId: String): MessageDto {
        if (this.isEncrypted && this.encryptedContent != null && signalProtocolManager != null) {
            try {
                val payloads = Json.decodeFromString<Map<String, EncryptedMessage>>(this.encryptedContent)
                val myPayload = payloads[currentUserId]
                if (myPayload != null) {
                    val senderId = this.senderId
                    val decryptedBytes = signalProtocolManager.decryptMessage(
                        senderId = senderId, // Decrypt using the session we have with the sender
                        message = myPayload
                    )
                    return this.copy(content = decryptedBytes.decodeToString())
                }
            } catch (e: Exception) {
                Napier.e("E2EE decryption failed for message ${this.id}", e)
            }
        }
        return this
    }

    private suspend fun ensureSession(userId: String) {
        if (signalProtocolManager != null && !signalProtocolManager.hasSession(userId)) {
            val keyDto = dataSource.getUserPublicKey(userId)
            if (keyDto != null) {
                val bundle = Json.decodeFromString<PreKeyBundle>(keyDto.publicKey)
                signalProtocolManager.processPreKeyBundle(userId, bundle)
            } else {
                throw Exception("Public key not found for user $userId")
            }
        }
    }

    override suspend fun getConversations(): Result<List<Conversation>> = try {
        val currentUserId = getCurrentUserId() ?: throw Exception("Not logged in")
        val conversationData = dataSource.getConversations()
        
        val conversations = kotlinx.coroutines.coroutineScope {
            conversationData.map { (participation, user, chatInfo) ->
                async {
                    var lastMessageText = "No messages yet"
                    val lastMessage = dataSource.getLastMessage(participation.chatId)
                    
                    if (lastMessage != null) {
                        try {
                            val decryptedDto = lastMessage.decryptIfNecessary(currentUserId)
                            lastMessageText = decryptedDto.content
                        } catch (e: Exception) {
                            lastMessageText = "Encrypted message"
                        }
                    }
                    
                    val unreadCount = dataSource.getUnreadCount(participation.chatId, participation.lastReadAt)
                    
                    val isGroup = chatInfo?.isGroup ?: false
                    val groupName = chatInfo?.name
                    val groupAvatar = chatInfo?.avatarUrl

                    Conversation(
                        chatId = participation.chatId,
                        participantId = user?.uid ?: participation.userId,
                        participantName = if (isGroup) groupName ?: "Group Chat" else (user?.displayName ?: user?.username ?: "Unknown"),
                        participantAvatar = if (isGroup) groupAvatar else user?.avatar,
                        lastMessage = lastMessageText,
                        lastMessageTime = lastMessage?.createdAt,
                        unreadCount = unreadCount,
                        isOnline = if (isGroup) false else (user?.status?.name == "ONLINE"),
                        isGroup = isGroup
                    )
                }
            }.map { it.await() }
        }.sortedByDescending { it.lastMessageTime ?: "" } // Use empty string to sort nulls to bottom
        
        Result.success(conversations)
    } catch (e: Exception) {
        Napier.e("Error getting conversations", e)
        Result.failure(e)
    }

    override suspend fun getMessages(chatId: String, limit: Int, before: String?): Result<List<Message>> = try {
        val currentUserId = getCurrentUserId() ?: throw Exception("Not logged in")
        val messageDtos = dataSource.getMessages(chatId, limit, before)
        
        // Decrypt separately
        val decrypted = messageDtos.map { 
            it.decryptIfNecessary(currentUserId).toDomain() 
        }
        Result.success(decrypted)
    } catch (e: Exception) {
        Napier.e("Error getting messages: ${e.message}", e)
        Result.failure(e)
    }

    override suspend fun sendMessage(
        chatId: String, 
        content: String, 
        mediaUrl: String?, 
        messageType: String,
        expiresAt: String?
    ): Result<Message> = try {
        val currentUserId = getCurrentUserId() ?: throw Exception("Not logged in")
        
        var isEncrypted = false
        var encryptedContentStr: String? = null
        var finalContent = content
        
        if (signalProtocolManager != null) {
            try {
                // Look up the other participant from the database instead of parsing chatId
                val otherUserId = dataSource.getOtherParticipantId(chatId, currentUserId)
                if (otherUserId != null) {
                    // Ensure session with the receiver
                    ensureSession(otherUserId)
                    val encryptedForReceiver = signalProtocolManager.encryptMessage(otherUserId, content.encodeToByteArray())
                    
                    // Ensure session with ourselves (to read our own sent messages)
                    ensureSession(currentUserId)
                    val encryptedForSelf = signalProtocolManager.encryptMessage(currentUserId, content.encodeToByteArray())

                    val payloads = mapOf(
                        otherUserId to encryptedForReceiver,
                        currentUserId to encryptedForSelf
                    )
                    
                    isEncrypted = true
                    encryptedContentStr = Json.encodeToString(payloads)
                    finalContent = "Message is encrypted"
                } else {
                    Napier.w("Could not determine other participant for chat $chatId, sending unencrypted")
                }
            } catch (e: Exception) {
                Napier.e("E2EE encryption failed, sending unencrypted", e)
                // Fall through and send unencrypted instead of failing the entire send
            }
        }

        val message = dataSource.sendMessage(chatId, finalContent, mediaUrl, messageType, isEncrypted, encryptedContentStr, expiresAt)
        val decryptedDto = message.copy(content = content) // We already know the content! 
        Result.success(decryptedDto.toDomain())
    } catch (e: Exception) {
        Napier.e("Error sending message", e)
        Result.failure(e)
    }

    override suspend fun getOrCreateChat(otherUserId: String): Result<String> = try {
        val chatId = dataSource.getOrCreateChat(otherUserId) ?: throw Exception("Failed to create chat")
        Result.success(chatId)
    } catch (e: Exception) {
        Napier.e("Error creating chat", e)
        Result.failure(e)
    }

    override suspend fun markMessagesAsRead(chatId: String): Result<Unit> = try {
        dataSource.markMessagesAsRead(chatId)
        Result.success(Unit)
    } catch (e: Exception) {
        Napier.e("Error marking messages as read", e)
        Result.failure(e)
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> = try {
        dataSource.deleteMessage(messageId)
        Result.success(Unit)
    } catch (e: Exception) {
        Napier.e("Error deleting message", e)
        Result.failure(e)
    }

    override suspend fun deleteMessageForMe(messageId: String): Result<Unit> = try {
        dataSource.deleteMessageForMe(messageId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun editMessage(messageId: String, newContent: String): Result<Unit> = try {
        val currentUserId = getCurrentUserId() ?: throw Exception("Not logged in")
        val originalMessage = dataSource.getMessageById(messageId) ?: throw Exception("Message not found")
        
        var isEncrypted = false
        var encryptedContentStr: String? = null
        var finalContent = newContent
        
        if (signalProtocolManager != null && originalMessage.isEncrypted) {
            try {
                val chatId = originalMessage.chatId
                val otherUserId = dataSource.getOtherParticipantId(chatId, currentUserId)
                
                if (otherUserId != null) {
                    ensureSession(otherUserId)
                    val encryptedForReceiver = signalProtocolManager.encryptMessage(otherUserId, newContent.encodeToByteArray())
                    
                    ensureSession(currentUserId)
                    val encryptedForSelf = signalProtocolManager.encryptMessage(currentUserId, newContent.encodeToByteArray())

                    val payloads = mapOf(
                        otherUserId to encryptedForReceiver,
                        currentUserId to encryptedForSelf
                    )
                    
                    isEncrypted = true
                    encryptedContentStr = Json.encodeToString(payloads)
                    finalContent = "Message is encrypted"
                }
            } catch (e: Exception) {
                Napier.e("E2EE encryption failed for edit, sending unencrypted", e)
            }
        }

        dataSource.editMessage(messageId, finalContent, isEncrypted, encryptedContentStr)
        Result.success(Unit)
    } catch (e: Exception) {
        Napier.e("Error editing message", e)
        Result.failure(e)
    }



    override suspend fun uploadMedia(chatId: String, fileBytes: ByteArray, fileName: String, contentType: String): Result<String> = try {
        val url = dataSource.uploadMedia(chatId, fileBytes, fileName, contentType)
        Result.success(url)
    } catch (e: Exception) {
        Napier.e("Error uploading media", e)
        Result.failure(e)
    }

    override suspend fun broadcastTypingStatus(chatId: String, isTyping: Boolean): Result<Unit> = try {
        dataSource.broadcastTypingStatus(chatId, isTyping)
        Result.success(Unit)
    } catch (e: Exception) {
        Napier.e("Error broadcasting typing status", e)
        Result.failure(e)
    }

    override fun subscribeToMessages(chatId: String): Flow<Message> =
        dataSource.subscribeToMessages(chatId).map { 
            val userId = getCurrentUserId() ?: ""
            it.decryptIfNecessary(userId).toDomain() 
        }

    override fun subscribeToInboxUpdates(chatIds: List<String>): Flow<Message> =
        dataSource.subscribeToInboxUpdates(chatIds).map { 
            val userId = getCurrentUserId() ?: ""
            it.decryptIfNecessary(userId).toDomain() 
        }

    override fun subscribeToTypingStatus(chatId: String): Flow<TypingStatus> =
        dataSource.subscribeToTypingStatus(chatId).map { data ->
            TypingStatus(
                userId = data["user_id"] as? String ?: "",
                chatId = chatId,
                isTyping = data["is_typing"] as? Boolean ?: false
            )
        }

    override fun subscribeToReadReceipts(chatId: String): Flow<Message> =
        dataSource.subscribeToReadReceipts(chatId).map { 
            val userId = getCurrentUserId() ?: ""
            it.decryptIfNecessary(userId).toDomain() 
        }

    override suspend fun initializeE2EE(): Result<Unit> = try {
        if (signalProtocolManager != null) {
            try {
                signalProtocolManager.getLocalRegistrationId()
            } catch (e: Exception) {
                // If this throws, we don't have local keys generated yet.
                val identity = signalProtocolManager.generateIdentityAndKeys()
                val preKeys = signalProtocolManager.generateOneTimePreKeys(startId = 1, count = 100)
                
                val bundle = PreKeyBundle(
                    registrationId = identity.registrationId,
                    deviceId = 1,
                    preKeyId = preKeys.firstOrNull()?.keyId,
                    preKeyPublic = preKeys.firstOrNull()?.publicKey,
                    signedPreKeyId = identity.signedPreKeyId,
                    signedPreKeyPublic = identity.signedPreKey,
                    signedPreKeySignature = identity.signedPreKeySignature,
                    identityKey = identity.identityKey
                )
                
                val bundleStr = Json.encodeToString(bundle)
                dataSource.uploadUserPublicKey(bundleStr)
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Napier.e("Error initializing E2EE keys", e)
        Result.failure(e)
    }

    override fun getCurrentUserId(): String? = dataSource.getCurrentUserId()
    override suspend fun createGroupChat(name: String, participantIds: List<String>, avatarUrl: String?): Result<String> = try {
        val chatId = dataSource.createGroupChat(name, participantIds, avatarUrl) ?: throw Exception("Failed to create group chat")
        Result.success(chatId)
    } catch (e: Exception) {
        io.github.aakira.napier.Napier.e("Error creating group chat", e)
        Result.failure(e)
    }

    override suspend fun getGroupMembers(chatId: String): Result<List<Pair<com.synapse.social.studioasinc.shared.domain.model.User, Boolean>>> = try {
        val members = dataSource.getGroupMembers(chatId)
        Result.success(members)
    } catch (e: Exception) {
        io.github.aakira.napier.Napier.e("Error getting group members", e)
        Result.failure(e)
    }

    override suspend fun addGroupMember(chatId: String, userId: String): Result<Unit> = try {
        dataSource.addGroupMember(chatId, userId)
        Result.success(Unit)
    } catch (e: Exception) {
        io.github.aakira.napier.Napier.e("Error adding group member", e)
        Result.failure(e)
    }

    override suspend fun removeGroupMember(chatId: String, userId: String): Result<Unit> = try {
        dataSource.removeGroupMember(chatId, userId)
        Result.success(Unit)
    } catch (e: Exception) {
        io.github.aakira.napier.Napier.e("Error removing group member", e)
        Result.failure(e)
    }

}