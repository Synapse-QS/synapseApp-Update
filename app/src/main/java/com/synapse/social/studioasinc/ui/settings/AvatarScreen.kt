package com.synapse.social.studioasinc.ui.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.core.util.ImageLoader
import com.synapse.social.studioasinc.presentation.editprofile.EditProfileEvent
import com.synapse.social.studioasinc.presentation.editprofile.EditProfileViewModel

@Composable
private fun AvatarPreviewCard(
    avatarUrl: String?,
    isUploading: Boolean
) {
    val context = LocalContext.current
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = SettingsShapes.cardShape,
        color = SettingsColors.cardBackgroundElevated,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    if (avatarUrl != null && avatarUrl.isNotBlank()) {
                        AsyncImage(
                            model = ImageLoader.buildImageRequest(context, avatarUrl),
                            contentDescription = "Current profile photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_person),
                                    contentDescription = "Default avatar",
                                    modifier = Modifier.size(56.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = if (isUploading) "Uploading..." else "Current Profile Photo",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            if (!isUploading) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose an option below to update",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarScreen(
    viewModel: AvatarViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    var showRemoveDialog by remember { mutableStateOf(false) }
    val isRemoving by viewModel.isRemoving.collectAsState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.uploadPhoto(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.uploadBitmap(bitmap)
        }
    }

    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove Profile Photo") },
            text = { Text("Are you sure you want to remove your profile photo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeProfilePhoto(
                            onSuccess = {
                                showRemoveDialog = false
                                Toast.makeText(context, "Profile photo removed", Toast.LENGTH_SHORT).show()
                            },
                            onError = { error ->
                                showRemoveDialog = false
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    enabled = !isRemoving
                ) {
                    if (isRemoving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Remove")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text("Avatar") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = SettingsSpacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(SettingsSpacing.sectionSpacing)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AvatarPreviewCard(
                    avatarUrl = uiState.currentAvatarUrl,
                    isUploading = uiState.isUploading
                )
            }

            item {
                SettingsSection(title = "Profile Photo") {
                    SettingsNavigationItem(
                        title = "Choose from Gallery",
                        subtitle = "Select a photo from your device",
                        icon = R.drawable.ic_image,
                        position = SettingsItemPosition.Top,
                        onClick = {
                            try {
                                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            } catch (e: android.content.ActivityNotFoundException) {
                                Toast.makeText(context, "No photo picker available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    SettingsNavigationItem(
                        title = "Take Photo",
                        subtitle = "Capture a new photo with camera",
                        icon = R.drawable.ic_camera,
                        position = SettingsItemPosition.Middle,
                        onClick = {
                            try {
                                cameraLauncher.launch(null)
                            } catch (e: android.content.ActivityNotFoundException) {
                                Toast.makeText(context, "No camera available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    SettingsNavigationItem(
                        title = "Remove Profile Photo",
                        subtitle = "Use default avatar",
                        icon = R.drawable.ic_delete,
                        position = SettingsItemPosition.Bottom,
                        onClick = { showRemoveDialog = true }
                    )
                }
            }

            item {
                SettingsSection(title = "Avatar Creation") {
                    SettingsNavigationItem(
                        title = "Create Avatar",
                        subtitle = "Design a custom avatar",
                        icon = R.drawable.ic_add,
                        position = SettingsItemPosition.Top,
                        onClick = {
                            Toast.makeText(context, "Avatar creator coming soon", Toast.LENGTH_SHORT).show()
                        }
                    )
                    SettingsNavigationItem(
                        title = "Edit Avatar",
                        subtitle = "Modify existing avatar",
                        icon = R.drawable.ic_edit,
                        position = SettingsItemPosition.Bottom,
                        onClick = {
                            Toast.makeText(context, "Avatar editor coming soon", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
