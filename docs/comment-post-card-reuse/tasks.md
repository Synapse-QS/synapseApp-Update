# Implementation Plan: Comment Post Card Reuse

## Overview

This implementation consolidates comment UI rendering by extending PostCard to handle both posts and comments. The approach maintains backward compatibility while eliminating ~400 lines of duplicate code from CommentItem and FeedCommentItem components.

Key implementation strategy:
- Extend PostCardState with optional comment-specific fields
- Create mapper functions for CommentWithUser → PostCardState
- Adapt PostCard rendering logic with conditional display
- Replace CommentItem and FeedCommentItem usage in screens
- Maintain Clean Architecture boundaries throughout

## Tasks

- [x] 1. Extend PostCardState with comment-specific fields
  - Add optional fields: isComment, parentCommentId, parentAuthorUsername, repliesCount, depth, showThreadLine, isLastReply
  - Ensure all new fields have default values for backward compatibility
  - Verify @Stable annotation remains for Compose optimization
  - _Requirements: 1.1, 1.2, 1.3, 1.4_

- [ ]* 1.1 Write property test for backward compatibility
  - **Property 1: Backward Compatibility for Post Mapping**
  - **Validates: Requirements 1.2**
  - Verify existing Post → PostCardState mapping produces isComment = false
  - Use Kotest with 100 iterations, generate random Post objects

- [ ] 2. Create comment mapper in PostUiMapper
  - [x] 2.1 Implement CommentWithUser → PostCardState mapper function
    - Create overloaded toPostCardState function accepting CommentWithUser
    - Map user information using CommentWithUser helper methods (getUsername, getAvatarUrl)
    - Create minimal Post object with comment content and metrics
    - Set isComment = true and populate comment-specific fields
    - Handle depth, showThreadLine, isLastReply parameters
    - Use TimeUtils.getTimeAgo for timestamp formatting
    - _Requirements: 2.1, 2.2, 2.3, 2.4, 2.5_
  
  - [ ]* 2.2 Write property test for comment discriminator flag
    - **Property 2: Comment Discriminator Flag**
    - **Validates: Requirements 1.3**
    - Verify all CommentWithUser mappings produce isComment = true
  
  - [ ]* 2.3 Write property test for depth preservation
    - **Property 3: Depth Preservation**
    - **Validates: Requirements 1.4**
    - Verify depth parameter is correctly preserved in PostCardState
  
  - [ ]* 2.4 Write property test for comprehensive comment mapping
    - **Property 4: Comprehensive Comment Mapping**
    - **Validates: Requirements 2.2, 2.3, 2.4**
    - Verify user info, content, and metrics are correctly mapped
    - Test with null user data to ensure graceful handling

- [ ] 3. Adapt PostCard for comment rendering
  - [x] 3.1 Implement conditional avatar sizing
    - Calculate avatar size based on isComment and depth
    - Use 32.dp for nested comments (depth > 0), 40.dp for top-level comments, 48.dp for posts
    - _Requirements: 3.5_
  
  - [ ]* 3.2 Write property test for avatar sizing by depth
    - **Property 6: Avatar Sizing by Depth**
    - **Validates: Requirements 3.5**
    - Verify correct avatar sizes for different depth values
  
  - [x] 3.3 Implement reply context display
    - Add "Replying to @username" text above comment content when parentAuthorUsername is not null
    - Use stringResource(R.string.replying_to) for text
    - Apply MaterialTheme.colorScheme.primary to username
    - Make username clickable for navigation
    - Use Spacing.ExtraSmall for padding
    - _Requirements: 4.1, 4.2, 4.3, 4.4_
  
  - [ ]* 3.4 Write property test for reply context display logic
    - **Property 7: Reply Context Display Logic**
    - **Validates: Requirements 4.1, 4.4**
    - Verify reply context shown only when parentAuthorUsername is not null
  
  - [x] 3.5 Implement thread line rendering
    - Add vertical line below avatar when showThreadLine is true
    - Use Spacing.Tiny (2.dp) width
    - Apply MaterialTheme.colorScheme.outlineVariant with 40% opacity
    - Extend from avatar to bottom of comment container
    - Hide when isLastReply is true
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_
  
  - [ ]* 3.6 Write property test for thread line display logic
    - **Property 8: Thread Line Display Logic**
    - **Validates: Requirements 5.1, 5.4**
    - Verify thread line shown only when showThreadLine is true and isLastReply is false
  
  - [x] 3.7 Hide post-specific features for comments
    - Conditionally hide repost button when isComment is true
    - Hide media attachments, polls, quoted posts for comments
    - Render only markdown text content for comments
    - Preserve all features for posts (no regression)
    - _Requirements: 3.4_
  
  - [ ]* 3.8 Write property test for post features hidden
    - **Property 5: Post Features Hidden for Comments**
    - **Validates: Requirements 3.4**
    - Verify post-specific UI elements not rendered when isComment is true

- [x] 4. Add string resource for reply context
  - Add "Replying to" string to app/src/main/res/values/strings.xml
  - Use key: replying_to
  - _Requirements: 4.1, 9.5_

- [x] 5. Checkpoint - Verify PostCard adaptations
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 6. Create FeedItem.CommentItem mapper
  - [x] 6.1 Implement FeedItem.CommentItem → PostCardState mapper
    - Create extension function or add to PostUiMapper
    - Map FeedItem.CommentItem fields to PostCardState
    - Set isComment = true
    - Preserve parentAuthorUsername for reply context
    - Handle null values gracefully
    - _Requirements: 7.3_
  
  - [ ]* 6.2 Write property test for FeedItem comment mapping
    - **Property 9: FeedItem Comment Mapping**
    - **Validates: Requirements 7.3**
    - Verify FeedItem.CommentItem correctly mapped with isComment = true

- [x] 7. Replace CommentItem in PostDetailScreen
  - [x] 7.1 Update PostDetailScreen to use PostCard for comments
    - Replace CommentItem composable calls with PostCard
    - Map CommentWithUser to PostCardState using new mapper
    - Calculate depth for nested comments
    - Determine showThreadLine based on reply presence
    - Set isLastReply for last comment in thread
    - Wire up callbacks: onCommentClick for reply, onLikeClick, onShareClick, onUserClick
    - _Requirements: 6.1, 6.2, 6.3_
  
  - [ ]* 7.2 Write unit tests for PostDetailScreen comment rendering
    - Test comment display with PostCard
    - Test nested comment depth calculation
    - Test thread line logic for comment trees
    - Test callback wiring (reply, like, share)
    - _Requirements: 6.5_

- [x] 8. Replace FeedCommentItem in FeedScreen
  - [x] 8.1 Update FeedScreen to use PostCard for feed comments
    - Replace FeedCommentItem composable calls with PostCard
    - Map FeedItem.CommentItem to PostCardState
    - Wire up callbacks: onCommentClick, onLikeClick, onShareClick, onUserClick
    - Handle navigation to parent post
    - _Requirements: 7.1, 7.2, 7.3_
  
  - [ ]* 8.2 Write unit tests for FeedScreen comment rendering
    - Test feed comment display with PostCard
    - Test callback wiring
    - Test navigation to parent post
    - _Requirements: 7.5_

- [x] 9. Checkpoint - Verify screen migrations
  - Ensure all tests pass, ask the user if questions arise.

- [x] 10. Add depth limiting safeguard
  - [x] 10.1 Implement MAX_COMMENT_DEPTH constant and clamping
    - Define MAX_COMMENT_DEPTH = 10 in PostUiMapper
    - Clamp depth parameter using coerceIn(0, MAX_COMMENT_DEPTH)
    - Apply in all mapper functions
    - _Requirements: 10.3_
  
  - [ ]* 10.2 Write property test for depth limiting
    - **Property 10: Depth Limiting**
    - **Validates: Requirements 10.3**
    - Verify depth never exceeds MAX_COMMENT_DEPTH

- [x] 11. Performance optimization
  - [x] 11.1 Verify stable state objects and recomposition optimization
    - Ensure PostCardState remains @Stable
    - Use remember and derivedStateOf for computed values in PostCard
    - Verify LazyColumn usage for comment lists
    - Profile recomposition count during scrolling
    - _Requirements: 10.1, 10.2, 10.4_
  
  - [ ]* 11.2 Write performance tests
    - Test recomposition count with comment list scrolling
    - Test memory usage with nested comment threads
    - Verify no more than 10% performance degradation vs. old components
    - _Requirements: 10.5_

- [x] 12. Delete deprecated components
  - [x] 12.1 Remove CommentItem.kt file
    - Verify no remaining references to CommentItem in codebase
    - Delete app/src/main/java/.../CommentItem.kt
    - _Requirements: 6.4_
  
  - [x] 12.2 Remove FeedCommentItem.kt file
    - Verify no remaining references to FeedCommentItem in codebase
    - Delete app/src/main/java/.../FeedCommentItem.kt
    - _Requirements: 7.4_

- [x] 13. Final verification and testing
  - [ ]* 13.1 Run full test suite
    - Execute all unit tests
    - Execute all property-based tests
    - Verify no regressions in existing post rendering
    - Verify comment rendering matches old behavior
  
  - [x] 13.2 Manual testing checklist
    - Test posts render correctly (no regression)
    - Test top-level and nested comment display
    - Test reply context display
    - Test thread line rendering
    - Test all interaction buttons (reply, like, share)
    - Test user navigation from comments
    - Test feed comment display
    - Verify no hardcoded colors, dimensions, or text
    - _Requirements: 3.1, 3.2, 3.3, 3.6, 8.1, 8.2, 8.3, 8.4, 8.5, 9.5_

- [x] 14. Final checkpoint - Complete implementation
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Property-based tests use Kotest with minimum 100 iterations
- All UI changes must use MaterialTheme colors and Spacing tokens
- No hardcoded text - use string resources from strings.xml
- Maintain Clean Architecture: UI mappers in app layer, domain models unchanged
- Delete deprecated components only after complete migration and verification
