package com.ishaan.kuluassignment.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Defines the set of shapes to be used by Material components in the app.
 * Material 3 specifies shapes for different container sizes: extra-small,
 * small, medium, large, and extra-large.
 */
val AppShapes = Shapes(
        // Use Case: For small components like Chips or Badges.
        // Example: An input chip in a search bar.
        extraSmall = RoundedCornerShape(4.dp),

        // Use Case: For small to medium-sized components.
        // Example: Buttons, TextFields, and small Cards.
        small = RoundedCornerShape(8.dp),

        // Use Case: The default shape for most medium-sized components.
        // Example: Standard Cards and Dialogs.
        medium = RoundedCornerShape(12.dp),

        // Use Case: For larger components.
        // Example: A large Card or a Modal Bottom Sheet.
        large = RoundedCornerShape(16.dp),

        // Use Case: For very large components that are screen-sized.
        // Example: The container for a Navigation Drawer or a large search bar.
        extraLarge = RoundedCornerShape(28.dp)
    )