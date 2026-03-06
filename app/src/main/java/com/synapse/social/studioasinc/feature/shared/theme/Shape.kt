package com.synapse.social.studioasinc.feature.shared.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Shapes
 * 
 * Expressive design uses rounder corners and more pronounced shapes
 * compared to standard Material 3.
 */
val ExpressiveShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Button-specific shapes for Material 3 Expressive
 * Use 16dp corner radius for a more friendly, approachable feel
 */
object ButtonShapes {
    val default = RoundedCornerShape(16.dp)
    val large = RoundedCornerShape(20.dp)
    val extraLarge = RoundedCornerShape(24.dp)
}

/**
 * Card shapes for Material 3 Expressive
 * Rounder corners create a softer, more premium appearance
 */
object CardShapes {
    val default = RoundedCornerShape(20.dp)
    val large = RoundedCornerShape(24.dp)
}
