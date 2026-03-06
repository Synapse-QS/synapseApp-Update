package com.synapse.social.studioasinc.feature.shared.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Example usage of Material 3 Expressive buttons
 * 
 * This file demonstrates how to use the new expressive button components.
 * Delete this file once you've migrated your buttons.
 */

@Preview(showBackground = true)
@Composable
private fun ExpressiveButtonsPreview() {
    SynapseTheme {
        Column(
            modifier = Modifier
                .padding(Spacing.Medium)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.SmallMedium)
        ) {
            // Primary action - most important
            ExpressiveButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Primary Action")
            }

            // With icon
            ExpressiveButtonWithIcon(
                onClick = { },
                text = "Like",
                icon = Icons.Filled.Favorite,
                modifier = Modifier.fillMaxWidth()
            )

            // Secondary action
            ExpressiveFilledTonalButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Secondary Action")
            }

            // Medium emphasis
            ExpressiveElevatedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Elevated Action")
            }

            // Lower emphasis
            ExpressiveOutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Outlined Action")
            }

            // Lowest emphasis
            ExpressiveTextButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Text Action")
            }
        }
    }
}

/**
 * Migration Guide:
 * 
 * Before (Standard Material 3):
 * ```
 * Button(onClick = { }) {
 *     Text("Click me")
 * }
 * ```
 * 
 * After (Material 3 Expressive):
 * ```
 * ExpressiveButton(onClick = { }) {
 *     Text("Click me")
 * }
 * ```
 * 
 * Or if you want to apply expressive styling to existing buttons:
 * ```
 * Button(
 *     onClick = { },
 *     shape = ButtonShapes.default,
 *     elevation = ButtonDefaults.buttonElevation(
 *         defaultElevation = 2.dp,
 *         pressedElevation = 6.dp
 *     )
 * ) {
 *     Text("Click me")
 * }
 * ```
 */
