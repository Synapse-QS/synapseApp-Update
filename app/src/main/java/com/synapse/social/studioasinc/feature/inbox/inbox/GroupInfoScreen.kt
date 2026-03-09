package com.synapse.social.studioasinc.feature.inbox.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.synapse.social.studioasinc.shared.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupInfoScreen(
    chatId: String,
    groupName: String,
    onNavigateBack: () -> Unit,
    viewModel: GroupInfoViewModel = hiltViewModel()
) {
    val members by viewModel.members.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUserId = viewModel.currentUserId

    var showAddDialog by remember { mutableStateOf(false) }
    var userIdToAdd by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadMembers(chatId)
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Member") },
            text = {
                OutlinedTextField(
                    value = userIdToAdd,
                    onValueChange = { userIdToAdd = it },
                    label = { Text("User ID") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addMember(userIdToAdd)
                        showAddDialog = false
                    },
                    enabled = userIdToAdd.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(groupName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val currentIsAdmin = members.find { it.first.uid == currentUserId }?.second == true
                    if (currentIsAdmin) {
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Default.PersonAdd, contentDescription = "Add Member")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = "Members (${members.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (isLoading && members.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(members, key = { it.first.uid }) { (user, isAdmin) ->
                        val isMe = user.uid == currentUserId
                        val currentIsAdmin = members.find { it.first.uid == currentUserId }?.second == true
                        GroupMemberItem(
                            user = user,
                            isAdmin = isAdmin,
                            isMe = isMe,
                            canRemove = currentIsAdmin && !isMe,
                            onRemove = { viewModel.removeMember(user.uid) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupMemberItem(
    user: User,
    isAdmin: Boolean,
    isMe: Boolean,
    canRemove: Boolean,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.avatar ?: "",
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = (user.displayName ?: user.username ?: "Unknown") + if (isMe) " (You)" else "",
                style = MaterialTheme.typography.bodyLarge
            )
            if (isAdmin) {
                Text("Admin", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
        if (canRemove) {
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.PersonRemove,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
