# Implementation Plan: Quote and Repost Feature

Implement the ability for users to reshare (repost) and quote posts with text, following Clean Architecture principles and maintaining UI consistency.

## Proposed Changes

### Domain Layer (shared/domain)

#### [NEW] [RepostPostUseCase.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/domain/usecase/post/RepostPostUseCase.kt)
- Create a use case that wraps `PostRepository.resharePost`.

#### [NEW] [QuotePostUseCase.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/domain/usecase/post/QuotePostUseCase.kt)
- Create a use case that wraps `PostRepository.quotePost`.

---

### Navigation Layer (app/src/main/java/com/synapse/social/studioasinc/feature/shared/navigation)

#### [MODIFY] [AppDestination.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/navigation/AppDestination.kt)
- Add [QuotePost(val postId: String)](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/createpost/quote/QuotePostScreen.kt#17-97) to [AppDestination](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/navigation/AppDestination.kt#3-45) sealed interface.

#### [MODIFY] [AppNavigation.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/navigation/AppNavigation.kt)
- Add a `composable` destination for `AppDestination.QuotePost`.
- Integrate [QuotePostScreen](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/createpost/quote/QuotePostScreen.kt#17-97) within this destination.

---

### UI Layer (app/src/main/java/com/synapse/social/studioasinc/feature)

#### [MODIFY] [FeedViewModel.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/home/home/FeedViewModel.kt)
- Introduce a `navigationEvent` or similar mechanism to handle Quote navigation from the interaction bar.
- Update [resharePost](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/PostRepository.kt#185-204) to use the new use case if available.

#### [MODIFY] [PostActionsFactory.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/components/post/PostActionsFactory.kt)
- Update `onQuote` callbacks to trigger the navigation flow.

#### [MODIFY] [PostUiMapper.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/components/post/PostCommon.kt)
- Ensure [PostCardState](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/shared/components/post/PostCard.kt#47-77) correctly populates `repostedBy` and `quotedPost` fields from the domain [Post](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/domain/model/Post.kt#58-241) model.

---

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to ensure no compilation errors or regressions in dependency injection.

### Manual Verification
1. **Repost Flow**:
   - Find a post in the feed.
   - Click the Repost button -> select "Reshare".
   - Confirm the repo call succeeds and feed refreshes (or shows optimistic update).
2. **Quote Flow**:
   - Click the Repost button -> select "Quote".
   - Verify navigation to [QuotePostScreen](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/createpost/quote/QuotePostScreen.kt#17-97).
   - Enter text and submit.
   - Verify the new post appears in the feed with the original post nested inside.
3. **UI Consistency**:
   - Verify the quoted post header (avatar, name, username) aligns with the project's design standards.
