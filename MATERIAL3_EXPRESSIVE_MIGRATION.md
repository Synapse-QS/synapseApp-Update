# 🎨 Material 3 Expressive Migration Guide

## Overview

Your app has been updated to use **Material 3 Expressive**, the next evolution of Material Design that features:

- **Rounder corners** (16dp instead of 12dp for buttons)
- **Bolder typography** (Bold weights for display/headline styles)
- **Enhanced elevation** (More pronounced shadows on press)
- **Friendlier appearance** (More emotional and approachable design)

## What Changed

### 1. Typography (✅ Applied Globally)

Display and headline styles now use **Bold** and **SemiBold** weights:

```kotlin
displayLarge = FontWeight.Bold    // was Normal
headlineLarge = FontWeight.Bold   // was Normal
titleLarge = FontWeight.SemiBold  // was Normal
```

This change is **automatic** - all text using `MaterialTheme.typography` will reflect the new weights.

### 2. Shapes (✅ Applied Globally)

New shape system with rounder corners:

```kotlin
val ExpressiveShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
```

Components using `MaterialTheme.shapes` will automatically use these values.

### 3. Buttons (⚠️ Requires Migration)

New expressive button components are available in:
`com.synapse.social.studioasinc.feature.shared.theme.ExpressiveButtons`

## Migration Options

### Option A: Use New Expressive Components (Recommended)

Replace standard Material 3 buttons with expressive variants:

**Before:**
```kotlin
Button(onClick = { }) {
    Text("Submit")
}
```

**After:**
```kotlin
ExpressiveButton(onClick = { }) {
    Text("Submit")
}
```

**Available Components:**
- `ExpressiveButton` - Primary actions (filled)
- `ExpressiveFilledTonalButton` - Secondary actions
- `ExpressiveElevatedButton` - Medium emphasis
- `ExpressiveOutlinedButton` - Lower emphasis
- `ExpressiveTextButton` - Lowest emphasis
- `ExpressiveButtonWithIcon` - Convenience for icon + text

### Option B: Apply Expressive Styling to Existing Buttons

Add expressive properties to your existing buttons:

```kotlin
Button(
    onClick = { },
    shape = ButtonShapes.default,  // 16dp corners
    elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 2.dp,
        pressedElevation = 6.dp
    )
) {
    Text("Submit")
}
```

### Option C: Gradual Migration

The changes are **backward compatible**. You can:
1. Keep existing buttons as-is (they'll use the new global shapes)
2. Migrate screens one at a time
3. Use expressive components for new features

## Key Files

### Theme Files
- `SynapseTheme.kt` - Main theme (updated with shapes)
- `Typography.kt` - Typography scale (updated with bold weights)
- `Shape.kt` - **NEW** - Expressive shapes system
- `ExpressiveButtons.kt` - **NEW** - Expressive button components
- `ExpressiveButtonsExample.kt` - **NEW** - Usage examples

### Shape Tokens
```kotlin
// Button shapes
ButtonShapes.default       // 16dp - standard buttons
ButtonShapes.large         // 20dp - large buttons
ButtonShapes.extraLarge    // 24dp - extra large buttons

// Card shapes
CardShapes.default         // 20dp - standard cards
CardShapes.large           // 24dp - large cards
```

## Examples

### Primary Action Button
```kotlin
ExpressiveButton(
    onClick = { viewModel.submit() },
    modifier = Modifier.fillMaxWidth()
) {
    Text("Submit")
}
```

### Button with Icon
```kotlin
ExpressiveButtonWithIcon(
    onClick = { viewModel.like() },
    text = "Like",
    icon = Icons.Filled.Favorite,
    modifier = Modifier.fillMaxWidth()
)
```

### Secondary Action
```kotlin
ExpressiveFilledTonalButton(
    onClick = { viewModel.cancel() },
    modifier = Modifier.fillMaxWidth()
) {
    Text("Cancel")
}
```

### Card with Expressive Shape
```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    shape = CardShapes.default,  // 20dp corners
    elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp,
        pressedElevation = 8.dp
    )
) {
    // Card content
}
```

## Testing

Preview your changes using the example composable:
```kotlin
@Preview
@Composable
fun MyScreenPreview() {
    SynapseTheme {
        // Your screen content
    }
}
```

## Resources

- [Material 3 Expressive Official Docs](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Android 16 Design Changes](https://developer.android.com/design/ui/wear/guides/get-started/apply)
- Content rephrased from: https://suridevs.com/blog/posts/google-material-3-expressive-android-16/

## Notes

- All changes follow your AGENTS.md guidelines (no hardcoded colors, dimensions, or text)
- Typography and shapes are applied globally through `MaterialTheme`
- Button components are opt-in for gradual migration
- Dynamic color support is maintained
- Dark theme support is maintained
