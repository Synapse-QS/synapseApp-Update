# Task Checklist - X-Style Nested Comments

- [x] Planning & Design
    - [x] Research existing implementation
    - [/] Define the nesting navigation flow
    - [/] Draft Implementation Plan
- [ ] Backend/Data Layer
    - [ ] Update [CommentPagingSource](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/paging/CommentPagingSource.kt#8-39) to support `parentCommentId`
    - [ ] (Optional) Update [CommentRepository](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/data/repository/CommentRepository.kt#31-651) if needed for better nesting support
- [ ] Navigation
    - [ ] Update `PostDetailNavigation` to handle comment IDs
    - [ ] OR Create `CommentDetailNavigation`
- [ ] ViewModel/UI Layer
    - [ ] Create `CommentDetailViewModel` (or refactor [PostDetailViewModel](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailViewModel.kt#30-388))
    - [ ] Create `CommentDetailScreen` (or refactor [PostDetailScreen](file:///c:/Users/Ashik/Documents/synapseApp/app/src/main/java/com/synapse/social/studioasinc/feature/post/postdetail/PostDetailScreen.kt#42-231))
    - [ ] Update `CommentItem` to navigate on click
- [ ] Verification
    - [ ] Verify navigation from post to comment detail
    - [ ] Verify navigation from comment detail to nested comment detail
    - [ ] Verify posting replies in nested view
