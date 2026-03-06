package com.synapse.social.studioasinc.feature.shared.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Button Components
 * 
 * These buttons feature:
 * - Rounder corners (16dp instead of 12dp)
 * - More pronounced elevation on press
 * - Larger touch targets (48dp minimum)
 * - Enhanced visual feedback
 */

/**
 * Expressive Filled Button - Primary actions
 * 
 * Use for the most important action on a screen.
 */
@Composable
fun ExpressiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShapes.default,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * Expressive Filled Tonal Button - Secondary actions
 * 
 * Use for important but not primary actions.
 */
@Composable
fun ExpressiveFilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShapes.default,
        elevation = ButtonDefaults.filledTonalButtonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 4.dp
        ),
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * Expressive Elevated Button - Medium emphasis actions
 * 
 * Use when you need visual separation from the background.
 */
@Composable
fun ExpressiveElevatedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShapes.default,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 6.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * Expressive Outlined Button - Lower emphasis actions
 * 
 * Use for less important actions or when you need a lighter visual weight.
 */
@Composable
fun ExpressiveOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShapes.default,
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * Expressive Text Button - Lowest emphasis actions
 * 
 * Use for the least important actions or in dense UIs.
 */
@Composable
fun ExpressiveTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ButtonShapes.default,
        contentPadding = contentPadding,
        content = content
    )
}

/**
 * Convenience composable for buttons with icon and text
 */
@Composable
fun ExpressiveButtonWithIcon(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconDescription: String? = null
) {
    ExpressiveButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription
        )
        Spacer(Modifier.width(Spacing.Small))
        Text(text)
    }
}
