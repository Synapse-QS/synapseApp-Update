package com.synapse.social.studioasinc.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.synapse.social.studioasinc.R


@Composable
private fun EmptyFavouritesCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = SettingsShapes.cardShape,
        color = SettingsColors.cardBackgroundElevated,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_favorite),
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Favourites Yet",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Add contacts to your favourites for quick access",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text("Favourites") },
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
                EmptyFavouritesCard()
            }

            item {
                SettingsSection(title = "Manage Favourites") {
                    SettingsNavigationItem(
                        title = "Add to Favourites",
                        subtitle = "Select contacts to add to favourites",
                        icon = R.drawable.ic_add,
                        position = SettingsItemPosition.Top,
                        onClick = { }
                    )
                    SettingsNavigationItem(
                        title = "Reorder Favourites",
                        subtitle = "Change the order of favourite contacts",
                        icon = R.drawable.ic_reorder,
                        position = SettingsItemPosition.Middle,
                        onClick = { }
                    )
                    SettingsNavigationItem(
                        title = "Remove from Favourites",
                        subtitle = "Remove contacts from favourites list",
                        icon = R.drawable.ic_delete,
                        position = SettingsItemPosition.Bottom,
                        onClick = { }
                    )
                }
            }

            item {
                SettingsSection(title = "Display Options") {
                    SettingsToggleItem(
                        title = "Show Favourites in Chat List",
                        subtitle = "Display favourite contacts at the top",
                        icon = R.drawable.ic_chat,
                        checked = true,
                        onCheckedChange = { },
                        position = SettingsItemPosition.Top
                    )
                    SettingsToggleItem(
                        title = "Show Favourite Badge",
                        subtitle = "Display a star icon on favourite contacts",
                        icon = R.drawable.ic_favorite,
                        checked = true,
                        onCheckedChange = { },
                        position = SettingsItemPosition.Bottom
                    )
                }
            }
        }
    }
}
