# Implementation Plan - Chat Settings and Folders

Implement "Chat Settings" and "Chat Folders" with features inspired by Telegram to enhance user customization and organization.

## Proposed Changes

### Domain Layer

#### [MODIFY] [ChatSettingsModels.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/domain/model/ChatSettingsModels.kt)
- Add `ChatListLayout` enum (SINGLE_LINE, DOUBLE_LINE).
- Add `ChatSwipeGesture` enum (ARCHIVE, DELETE, MUTE, PIN, READ).
- Add `ChatFolder` data class (id, name, icon, includedChatIds, excludedChatIds, folderFilters).
- Add `ChatSettings` data class to aggregate all chat-specific settings.

### Data Layer

#### [MODIFY] [SettingsDataStore.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/local/database/SettingsDataStore.kt)
- Add DataStore keys for:
    - `chat_message_corner_radius` (Int)
    - `chat_list_layout` (String)
    - `chat_swipe_gesture` (String)
    - `chat_folders` (String - JSON serialized)
- Implement getter flows and setter methods for these new keys.

#### [MODIFY] [SettingsRepository.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/SettingsRepository.kt)
- Define interface methods for the new chat settings and folders.

#### [MODIFY] [SettingsRepositoryImpl.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/SettingsRepositoryImpl.kt)
- Implement the new interface methods using [SettingsDataStore](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/local/database/SettingsDataStore.kt#38-717).

### UI Layer

#### [NEW] [ChatSettingsScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/ChatSettingsScreen.kt)
- Implement the UI for chat customization.
- Features: 
    - Live bubble preview (showing text size and corner radius changes).
    - Slider for text size (12sp - 30sp).
    - Slider for bubble corner radius (0dp - 24dp).
    - Wallpaper selection (Solid colors, presets).
    - Theme preset selection.
    - Chat list layout toggle (1-line vs 2-line).
    - Swipe gesture selection.

#### [NEW] [ChatSettingsViewModel.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/ChatSettingsViewModel.kt)
- Manage state for the Chat Settings screen.

#### [NEW] [ChatFoldersScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/ChatFoldersScreen.kt)
- UI for creating and managing chat folders.

#### [NEW] [ChatFoldersViewModel.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/ChatFoldersViewModel.kt)
- Manage state for Chat Folders.

#### [MODIFY] [SettingsDestination.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/SettingsDestination.kt)
- Add `ChatSettings` and `ChatFolders` destinations.

#### [MODIFY] [SettingsNavHost.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/SettingsNavHost.kt)
- Add composable routes for the new screens.

#### [MODIFY] [SettingsHubScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/ui/settings/SettingsHubScreen.kt)
- Add menu items for "Chat Settings" and "Chat Folders".

### Integration

#### [MODIFY] [ChatsTabScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatsTabScreen.kt)
- Update [ConversationItem](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatsTabScreen.kt#93-204) to support 1-line or 2-line layout based on settings.
- Implement swipe gestures (using `SwipeToDismissBox`).

## Verification Plan

### Automated Tests
- Unit tests for [SettingsRepository](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/SettingsRepository.kt#18-234) to ensure new settings are correctly persisted and retrieved.
- Unit tests for `ChatFoldersViewModel` logic.

### Manual Verification
1. Navigate to Settings -> Chat Settings.
2. Adjust text size and observe the live preview bubble.
3. Adjust corner radius and observe the live preview.
4. Change wallpaper and theme.
5. Toggle "Chat List View" and check the Inbox screen to see if it changes from 2-line to 1-line.
6. Change the "Swipe Gesture" and verify the behavior in the Inbox screen.
7. Navigate to Chat Folders, create a new folder, and verify it appears (if implementing folder tabs in Inbox - will confirm if tabs are needed).
