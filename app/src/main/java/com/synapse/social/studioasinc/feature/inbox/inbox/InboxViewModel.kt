package com.synapse.social.studioasinc.feature.inbox.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapse.social.studioasinc.domain.model.User
import com.synapse.social.studioasinc.UserProfileManager
import com.synapse.social.studioasinc.shared.domain.model.chat.Conversation
import com.synapse.social.studioasinc.shared.util.TimestampFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

import com.synapse.social.studioasinc.shared.domain.usecase.chat.GetConversationsUseCase
import com.synapse.social.studioasinc.shared.domain.usecase.chat.SubscribeToInboxUpdatesUseCase
import com.synapse.social.studioasinc.shared.domain.usecase.chat.InitializeE2EUseCase
import com.synapse.social.studioasinc.data.repository.SettingsRepository
import com.synapse.social.studioasinc.domain.model.ChatListLayout
import com.synapse.social.studioasinc.domain.model.ChatSwipeGesture

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val subscribeToInboxUpdatesUseCase: SubscribeToInboxUpdatesUseCase,
    private val initializeE2EUseCase: InitializeE2EUseCase,
    private val chatLockManager: com.synapse.social.studioasinc.core.util.ChatLockManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _currentUserProfile = MutableStateFlow<User?>(null)
    val currentUserProfile: StateFlow<User?> = _currentUserProfile.asStateFlow()

    private val _conversations = MutableStateFlow<List<com.synapse.social.studioasinc.shared.domain.model.chat.Conversation>>(emptyList())
    val conversations: StateFlow<List<com.synapse.social.studioasinc.shared.domain.model.chat.Conversation>> = _conversations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val chatListLayout: StateFlow<ChatListLayout> = settingsRepository.chatListLayout
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatListLayout.DOUBLE_LINE
        )

    val chatSwipeGesture: StateFlow<ChatSwipeGesture> = settingsRepository.chatSwipeGesture
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatSwipeGesture.ARCHIVE
        )

    init {
        viewModelScope.launch {
            initializeE2EUseCase().onFailure { e ->
                _error.value = "Failed to initialize E2EE: ${e.message}"
            }
            loadCurrentUserProfile()
            loadConversations()
            subscribeToInboxUpdates()
        }
    }

    private suspend fun loadCurrentUserProfile() {
        try {
            val profile = UserProfileManager.getCurrentUserProfile()
            _currentUserProfile.value = profile
        } catch (_: Exception) { }
    }

    fun loadConversations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getConversationsUseCase().onSuccess { conversationList ->
                _conversations.value = conversationList
                _isLoading.value = false
                subscribeToInboxUpdates()
            }.onFailure { e ->
                _error.value = "Failed to load conversations: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private var inboxSubscriptionJob: kotlinx.coroutines.Job? = null

    private fun subscribeToInboxUpdates() {
        inboxSubscriptionJob?.cancel()
        val chatIds = _conversations.value.map { it.chatId }
        if (chatIds.isEmpty()) return

        inboxSubscriptionJob = viewModelScope.launch {
            subscribeToInboxUpdatesUseCase(chatIds).collect {
                loadConversations() // Simple reload on any new message
            }
        }
    }

    fun isChatLocked(chatId: String): Boolean = chatLockManager.isChatLocked(chatId)

    fun getFormattedTimestamp(timestamp: String?): String = TimestampFormatter.formatRelative(timestamp)
}
