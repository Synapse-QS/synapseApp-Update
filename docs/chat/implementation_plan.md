# Media & Rich Content Implementation Plan

Add support for sending and displaying images, videos, voice messages, and files in the chat feature.

## Proposed Changes

### UI Layer (`app/`)

#### [MODIFY] [ChatScreen.kt](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt)
- Add an `IconButton` (clip/attachment icon) next to the text field in the bottom bar.
- Implement a `rememberLauncherForActivityResult` with `ActivityResultContracts.PickVisualMedia()` for images and videos.
- Implement a `rememberLauncherForActivityResult` with `ActivityResultContracts.GetContent()` for general files.
- Add UI state for handling media selection and displaying a preview before sending (optional, but good for UX).

#### [MODIFY] [ChatViewModel.kt](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/ChatViewModel.kt)
- Implement [uploadAndSendMedia](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/ChatViewModel.kt#284-287) logic.
- Use `ContentResolver` to get file bytes from URIs.
- Call `uploadMediaUseCase` to upload to Supabase storage.
- Call `sendMessageUseCase` with the returned `mediaUrl` and appropriate `messageType`.

#### [MODIFY] [MessageBubble](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt)
- Update [MessageBubble](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/screens/ChatScreen.kt#345-487) to check `message.messageType`.
- If `IMAGE`, display using `AsyncImage`.
- If `VIDEO`, display a thumbnail with a play icon overlay.
- If `VOICE`, display a recording playback UI (waveform placeholder for now).
- If `FILE`, display a file icon with the filename.

### Data & Domain Layer (`shared/`)

#### [MODIFY] [SupabaseChatDataSource.kt](file:///c:/Users/Ashik/Downloads/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/datasource/SupabaseChatDataSource.kt)
- Ensure [uploadMedia](file:///c:/Users/Ashik/Downloads/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/repository/SupabaseChatRepository.kt#227-234) correctly handles Supabase Storage buckets.
- Verify [sendMessage](file:///c:/Users/Ashik/Downloads/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/inbox/inbox/ChatViewModel.kt#169-223) correctly persists `mediaUrl` and `messageType`.

## Verification Plan

### Automated Tests
- Run [SupabaseChatDataSourceTest.kt](file:///C:/Users/Ashik/Downloads/synapseApp/shared/src/commonTest/kotlin/com/synapse/social/studioasinc/shared/data/datasource/SupabaseChatDataSourceTest.kt) to ensure basic messaging still works.
- Add a new test case for media message serialization in `ChatDtosTest` (if exists, else create it).

### Manual Verification
1.  Open Chat with a user.
2.  Click the attachment icon.
3.  Select an image from the gallery.
4.  Verify the image is uploaded and displayed in the chat bubble.
5.  Repeat for a video.
6.  Verify that images/videos from the other participant are also visible.
7.  Test sending a PDF or DOCX file.
