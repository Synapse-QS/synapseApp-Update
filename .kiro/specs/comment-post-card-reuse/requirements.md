# Requirements Document

## Introduction

This feature consolidates the UI components for displaying comments by reusing the existing PostCard component infrastructure. Currently, comments use separate UI components (CommentItem, FeedCommentItem) that duplicate logic found in PostCard. By adapting PostCard to handle both posts and comments, we reduce code duplication, improve maintainability, and ensure consistent UI/UX across the application.

## Glossary

- **PostCard**: The existing Composable component that displays a post with user info, content, media, and interaction buttons
- **PostCardState**: The data class that holds the state for rendering a PostCard
- **CommentItem**: The current Composable component that displays a comment in post detail screens
- **FeedCommentItem**: The current Composable component that displays a comment in the main feed
- **Comment**: A domain model representing a user's comment on a post or another comment
- **CommentWithUser**: A domain model combining Comment data with associated User information
- **UI_Mapper**: A utility object that transforms domain models into UI state objects
- **Thread_Line**: A visual indicator showing parent-child relationships between comments
- **Reply_Context**: UI element showing which user/comment is being replied to

## Requirements

### Requirement 1: Extend PostCardState for Comments

**User Story:** As a developer, I want PostCardState to support comment data, so that I can use the same state model for both posts and comments.

#### Acceptance Criteria

1. THE PostCardState SHALL include optional fields for comment-specific data (parentCommentId, parentAuthorUsername, repliesCount, depth)
2. THE PostCardState SHALL maintain backward compatibility with existing post rendering
3. WHEN a PostCardState represents a comment, THE PostCardState SHALL include a flag or discriminator to identify it as a comment type
4. THE PostCardState SHALL support nested reply depth information for thread visualization

### Requirement 2: Create Comment-to-PostCardState Mapper

**User Story:** As a developer, I want to map Comment domain models to PostCardState, so that I can render comments using PostCard.

#### Acceptance Criteria

1. THE UI_Mapper SHALL provide a function that converts CommentWithUser to PostCardState
2. WHEN mapping a comment, THE UI_Mapper SHALL populate user information from the CommentWithUser.user field
3. WHEN mapping a comment, THE UI_Mapper SHALL map comment content to the post text field
4. WHEN mapping a comment, THE UI_Mapper SHALL map comment metrics (likes, replies) to corresponding PostCardState fields
5. THE UI_Mapper SHALL handle null or missing user data gracefully with default values

### Requirement 3: Adapt PostCard for Comment Display

**User Story:** As a user, I want comments to display with the same visual quality as posts, so that the interface feels consistent.

#### Acceptance Criteria

1. WHEN PostCard renders a comment, THE PostCard SHALL display user avatar, username, and timestamp
2. WHEN PostCard renders a comment, THE PostCard SHALL render markdown content correctly
3. WHEN PostCard renders a comment, THE PostCard SHALL show interaction buttons (reply, like, share)
4. WHEN PostCard renders a comment, THE PostCard SHALL hide post-specific features (repost button, media attachments, polls, quoted posts)
5. WHEN PostCard renders a comment, THE PostCard SHALL apply appropriate sizing (smaller avatar for nested replies)
6. THE PostCard SHALL use MaterialTheme colors and Spacing tokens (no hardcoded values)

### Requirement 4: Display Reply Context

**User Story:** As a user, I want to see who a comment is replying to, so that I can understand the conversation thread.

#### Acceptance Criteria

1. WHEN a comment is a reply, THE PostCard SHALL display "Replying to @username" above the comment content
2. THE Reply_Context SHALL use MaterialTheme.colorScheme.primary for the text color
3. THE Reply_Context SHALL be clickable to navigate to the parent comment or user
4. WHEN a comment is not a reply, THE PostCard SHALL NOT display reply context

### Requirement 5: Render Thread Lines for Nested Comments

**User Story:** As a user, I want to see visual lines connecting nested comments, so that I can follow conversation threads easily.

#### Acceptance Criteria

1. WHEN a comment has replies, THE PostCard SHALL display a vertical Thread_Line below the avatar
2. THE Thread_Line SHALL use MaterialTheme.colorScheme.outlineVariant with 40% opacity
3. THE Thread_Line SHALL be 2dp wide
4. WHEN a comment is the last reply in a thread, THE PostCard SHALL NOT display a Thread_Line
5. THE Thread_Line SHALL extend from the avatar to the bottom of the comment container

### Requirement 6: Replace CommentItem with PostCard

**User Story:** As a developer, I want to remove the CommentItem component, so that we have a single source of truth for rendering content.

#### Acceptance Criteria

1. THE PostDetailScreen SHALL use PostCard instead of CommentItem for rendering comments
2. WHEN replacing CommentItem, THE implementation SHALL maintain all existing functionality (reply, like, options, user navigation)
3. WHEN replacing CommentItem, THE implementation SHALL preserve thread visualization (depth, thread lines)
4. THE CommentItem.kt file SHALL be deleted after successful migration
5. THE implementation SHALL pass all existing tests for comment display and interaction

### Requirement 7: Replace FeedCommentItem with PostCard

**User Story:** As a developer, I want to remove the FeedCommentItem component, so that feed comments use the same rendering logic as other comments.

#### Acceptance Criteria

1. THE FeedScreen SHALL use PostCard instead of FeedCommentItem for rendering comments in the feed
2. WHEN replacing FeedCommentItem, THE implementation SHALL maintain all existing functionality (navigation, like, reply)
3. WHEN replacing FeedCommentItem, THE implementation SHALL handle FeedItem.CommentItem data correctly
4. THE FeedCommentItem.kt file SHALL be deleted after successful migration
5. THE implementation SHALL pass all existing tests for feed comment display

### Requirement 8: Handle Comment-Specific Actions

**User Story:** As a user, I want to interact with comments using reply, like, and share actions, so that I can engage with the conversation.

#### Acceptance Criteria

1. WHEN a user clicks the reply button on a comment, THE PostCard SHALL trigger the onCommentClick callback with the comment ID
2. WHEN a user clicks the like button on a comment, THE PostCard SHALL trigger the onLikeClick callback
3. WHEN a user clicks the share button on a comment, THE PostCard SHALL trigger the onShareClick callback
4. WHEN a user long-presses the like button on a comment, THE PostCard SHALL display the reaction picker
5. THE PostCard SHALL display the correct like state (liked/not liked) based on comment.userReaction

### Requirement 9: Maintain Architectural Boundaries

**User Story:** As a developer, I want the implementation to follow Clean Architecture, so that the codebase remains maintainable.

#### Acceptance Criteria

1. THE UI_Mapper SHALL reside in the UI layer (app/ directory)
2. THE implementation SHALL NOT introduce backend SDKs or types in the domain layer
3. THE implementation SHALL use StateFlow for state management in ViewModels
4. THE implementation SHALL delegate business logic to UseCases (no logic in Composables or ViewModels)
5. THE implementation SHALL use string resources from strings.xml (no hardcoded text)

### Requirement 10: Preserve Performance

**User Story:** As a user, I want comment rendering to be performant, so that scrolling through threads remains smooth.

#### Acceptance Criteria

1. WHEN rendering a list of comments, THE PostCard SHALL use stable state objects to minimize recomposition
2. THE PostCard SHALL use remember and derivedStateOf appropriately for computed values
3. WHEN rendering nested comments, THE implementation SHALL limit recursive depth to prevent performance degradation
4. THE implementation SHALL use LazyColumn for comment lists (no eager rendering)
5. WHEN a comment is updated (liked, replied to), THE PostCard SHALL only recompose the affected comment

