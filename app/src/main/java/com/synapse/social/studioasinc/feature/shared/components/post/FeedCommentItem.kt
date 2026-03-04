package com.synapse.social.studioasinc.feature.shared.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.TextView
import com.synapse.social.studioasinc.core.util.TimeUtils
import com.synapse.social.studioasinc.domain.model.FeedItem
import com.synapse.social.studioasinc.styling.MarkdownRenderer
import com.synapse.social.studioasinc.ui.components.CircularAvatar

@Composable
fun FeedCommentItem(
    commentItem: FeedItem.CommentItem,
    onCommentClick: (String, String) -> Unit, // (postId, commentId)
    onUserClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorOnSurface = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                // Navigate to the post detail to show the comment
                commentItem.parentPostId?.let { postId ->
                    onCommentClick(postId, commentItem.id)
                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        CircularAvatar(
            imageUrl = commentItem.avatarUrl,
            contentDescription = "Avatar",
            size = 48.dp,
            onClick = { onUserClick(commentItem.userId) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Content Column
        Column(modifier = Modifier.weight(1f)) {
            // Header: User Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = commentItem.userFullName.ifBlank { "User" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onUserClick(commentItem.userId) }
                )
                if (commentItem.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.Favorite, // Placeholder for verified badge
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp) // Adjust icon as needed
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "@${commentItem.username.ifBlank { "user" }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = " · ${TimeUtils.getTimeAgo(commentItem.createdAt ?: "") ?: "now"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { /* TODO Options */ },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Context: Replying to @user
            if (commentItem.parentAuthorUsername != null) {
                Text(
                    text = "Replying to @${commentItem.parentAuthorUsername}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Content
            AndroidView(
                modifier = Modifier.padding(vertical = 4.dp),
                factory = { ctx ->
                    TextView(ctx).apply {
                        setTextColor(colorOnSurface.toArgb())
                        textSize = 15f
                    }
                },
                update = { textView ->
                    MarkdownRenderer.get(context).render(textView, commentItem.content)
                }
            )

            // Actions: Replies, Likes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Comments Action
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            commentItem.parentPostId?.let { postId ->
                                onCommentClick(postId, commentItem.id)
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Reply",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    if (commentItem.commentCount > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = commentItem.commentCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Likes Action
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onLikeClick(commentItem.id) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (commentItem.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (commentItem.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    if (commentItem.likeCount > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = commentItem.likeCount.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (commentItem.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Share Action (Placeholder)
                IconButton(
                    onClick = { /* TODO Share */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
