package com.synapse.social.studioasinc.feature.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.synapse.social.studioasinc.shared.domain.usecase.presence.ObserveUserPresenceUseCase

@Composable
fun ActiveStatusIndicator(
    userId: String,
    observeUserPresenceUseCase: ObserveUserPresenceUseCase,
    size: Dp = 12.dp,
    modifier: Modifier = Modifier
) {
    val isOnline by observeUserPresenceUseCase(userId).collectAsState(initial = false)
    
    if (isOnline) {
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
        )
    }
}
