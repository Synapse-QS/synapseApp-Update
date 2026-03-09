# Implementation Plan - X-Style Nested Comments

Implement a recursive comment nesting system where clicking any comment navigates to a detail view for that specific comment thread, mirroring the UX of X (Twitter).

## Proposed Changes

### [Data Layer]

#### [MODIFY] [CommentPagingSource.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/paging/CommentPagingSource.kt)
- Update constructor to accept an optional `parentCommentId: String?`.
- Update the [load](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/paging/CommentPagingSource.kt#17-38) function to call a new specialized repository method if `parentCommentId` is present.

#### [MODIFY] [CommentRepository.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/CommentRepository.kt)
- Add `fetchPagedReplies(parentId, limit, offset)` to support paged loading of nested comments.
- Refactor [getReplies](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/CommentRepository.kt#100-140) if necessary to share logic with the paged version.

---

### [Navigation]

#### [MODIFY] [PostDetailNavigation.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/navigation/PostDetailNavigation.kt)
- Update routes or add a new `CommentDetail` route that accepts `commentId`.
- Ensure the navigation graph handles recursive calls to the same detail destination.

---

### [UI / Feature Layer]

#### [MODIFY] [PostDetailScreen.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailScreen.kt)
- Add a callback for comment clicks to navigate to the nested detail view.
- Update [PostDetailContent](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailScreen.kt#232-322) to handle showing a [Comment](file:///c:/Users/Ashik/Documents/synapseApp/shared/src/commonMain/kotlin/com/synapse/social/studioasinc/shared/domain/model/Comment.kt#6-27) as the header when navigating to a comment thread.

#### [MODIFY] [PostDetailViewModel.kt](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailViewModel.kt)
- Update `uiState` to hold information about whether it's displaying a Post or a Comment as the root.
- Update [loadPost](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailViewModel.kt#77-94) or add `loadCommentThread(commentId)` to initialize the state accordingly.
- Ensure the `commentsPagingFlow` uses the `parentCommentId` if the root is a comment.

---

### [Components]

#### [MODIFY] [CommentItem.kt] (Finding path during implementation)
- Add a clickable modifier to the comment body for navigation.

## Verification Plan

### Automated Tests
- N/A (Project currently relies on manual verification for UI flows)

### Manual Verification
1. Navigate to a Post with multiple comments.
2. Click on a comment.
3. Observe navigation to a new "Comment Detail" view where:
    - The clicked comment is at the top.
    - Its replies are listed below.
4. Reply to a nested comment and verify it appears in the list.
5. Click on a nested comment and verify it navigates further down (recursive navigation).
6. Verify the back button correctly navigates back up the thread tree.
