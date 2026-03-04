1. **Understand the issue:** The `getOrCreateChat` method currently makes a query for each chat the user is in to find a matching participant, leading to an N+1 query problem. This needs to be replaced with a more efficient single query using the Supabase `isIn` filter condition.

2. **Modify `ChatMessagingRepository.kt`:**
Replace the N+1 `for` loop in `app/src/main/java/com/synapse/social/studioasinc/data/repository/ChatMessagingRepository.kt`'s `getOrCreateChat` method with an efficient bulk query using `isIn("chat_id", chatIds)`.
```kotlin
            val myChats = client.from("chat_participants")
                .select(columns = Columns.list("chat_id")) {
                    filter { eq("user_id", currentUserId) }
                }.decodeList<ChatParticipantDto>()

            val chatIds = myChats.map { it.chatId }

            if (chatIds.isNotEmpty()) {
                val commonChat = client.from("chat_participants")
                    .select(columns = Columns.list("chat_id")) {
                        filter {
                            isIn("chat_id", chatIds)
                            eq("user_id", otherUserId)
                        }
                    }.decodeList<ChatParticipantDto>().firstOrNull()

                if (commonChat != null) {
                    return@withContext Result.success(commonChat.chatId)
                }
            }
```

3. **Modify `SupabaseChatDataSource.kt`:**
Similar to the repository, apply the same optimization in `shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/data/datasource/SupabaseChatDataSource.kt`'s `getOrCreateChat` method.
```kotlin
            val myChats = client.postgrest.from("chat_participants")
                .select(columns = Columns.list("chat_id")) {
                    filter { eq("user_id", currentUserId) }
                }.decodeList<ChatParticipantDto>()

            val chatIds = myChats.map { it.chatId }

            if (chatIds.isNotEmpty()) {
                val commonChat = client.postgrest.from("chat_participants")
                    .select(columns = Columns.list("chat_id")) {
                        filter {
                            isIn("chat_id", chatIds)
                            eq("user_id", otherUserId)
                        }
                    }.decodeList<ChatParticipantDto>().firstOrNull()

                if (commonChat != null) return@withContext commonChat.chatId
            }
```

4. **Verify correctness:** Build the application using `./gradlew build` to ensure the compilation succeeds and run `ktlint` checks. Ensure that `isIn` takes a `List<String>`.

5. **Complete pre-commit steps:** Follow instructions from the `pre_commit_instructions` tool to make sure tests and verification are correctly done before submission.
