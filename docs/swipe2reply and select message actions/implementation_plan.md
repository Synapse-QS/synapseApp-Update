# Implementation Plan - Chat Message Selection and Swipe-to-Reply

Implement multi-selection for chat messages with a contextual action bar and swipe-to-reply gesture.

## Proposed Changes

### Domain & Data Layer (Reply Support)

- #### [MODIFY] [ChatDtos.kt](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/dto/chat/ChatDtos.kt)
  - Add `reply_to_id` to [NewMessageDto](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/dto/chat/ChatDtos.kt#39-50).

- #### [MODIFY] [ChatRepository.kt](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/repository/ChatRepository.kt)
  - Add `replyToId: String? = null` to [sendMessage](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/repository/ChatRepository.kt#11-12) method signature.

- #### [MODIFY] [SupabaseChatRepository.kt](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/repository/SupabaseChatRepository.kt)
  - Pass `replyToId` through to the data source.

- #### [MODIFY] [SupabaseChatDataSource.kt](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/datasource/SupabaseChatDataSource.kt)
  - Include `replyToId` when creating [NewMessageDto](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/dto/chat/ChatDtos.kt#39-50).

- #### [MODIFY] [SendMessageUseCase.kt](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/usecase/chat/SendMessageUseCase.kt)
  - Update [invoke](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/usecase/chat/SendMessageUseCase.kt#7-14) to accept `replyToId`.

---

### UI Layer (Feature Implementation)

- #### [MODIFY] [ChatViewModel.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com\synapse\social\studioasinc\feature\inbox\inbox/ChatViewModel.kt)
  - Add `selectedMessageIds: StateFlow<Set<String>>`.
  - Add `replyingToMessage: StateFlow<Message?>`.
  - Implement `toggleMessageSelection`, `clearSelection`, `deleteSelectedMessages`, `copySelectedMessages`.
  - Implement `setReplyingToMessage`, `cancelReply`.
  - Update [sendMessage](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/repository/ChatRepository.kt#11-12) to include the `replyToId`.

- #### [MODIFY] [ChatScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com\synapse\social\studioasinc\feature\inbox\inbox\screens/ChatScreen.kt)
  - Update `TopAppBar` to show selection actions (Delete, Copy, Clear) when `selectedMessageIds` is not empty.
  - Update [MessageBubble](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt#557-760) to handle selection UI and gestures.
  - Add `SwipeToReply` gesture to [MessageBubble](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt#557-760).
  - Add `ReplyPreview` UI above the message input field.
  - Show the replied-to message inside [MessageBubble](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt#557-760) if `replyToId` is present.

## Verification Plan

### Manual Verification
1. **Message Selection**:
   - Long-press a message to enter selection mode.
   - Tap other messages to select multiple.
   - Verify selection count in TopAppBar.
   - Test "Copy" and "Delete" actions.
   - Test "Clear Selection" button.
2. **Swipe to Reply**:
   - Swipe a message (left/right) to trigger reply.
   - Verify reply preview appears above the input field.
   - Send the message and verify it displays the replied-to snippet.
   - Close the reply preview and verify it's cancelled.
3. **Combined Flow**:
   - Verify selection mode works independently of the reply flow.
