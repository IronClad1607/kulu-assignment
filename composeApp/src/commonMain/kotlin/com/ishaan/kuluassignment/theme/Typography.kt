package com.ishaan.kuluassignment.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * The main typography definition for the app, configured with the Lato font family.
 * Each style includes comments describing its primary use case with component examples.
 */
@Composable
fun getAppTypography(): Typography {
    return Typography(
        // Use Case: Very large, expressive text, for short, standalone headlines.
        // Example: A hero title on a marketing or landing page (e.g., "Welcome!").
        displayLarge = TextStyle(
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),

        // Use Case: Second-largest display text.
        // Example: A prominent feature quote or a key metric on a dashboard.
        displayMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: Smallest display text.
        // Example: A sub-heading for a prominent marketing section.
        displaySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: Large headlines, smaller than display styles.
        // Example: The title of a specific screen, like "Create a new contact".
        headlineLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: Medium-sized headlines.
        // Example: A section header within a screen, e.g., "Notifications" in a Settings page.
        headlineMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: The smallest headline style.
        // Example: A sub-section header, e.g., "Sound & Vibration".
        headlineSmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: The primary title on a screen.
        // Example: The main title in a TopAppBar.
        titleLarge = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),

        // Use Case: Medium-emphasis text, shorter than body text.
        // Example: The title of a list item or a card.
        titleMedium = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),

        // Use Case: The smallest title, for lower-emphasis text.
        // Example: A title for a smaller component or a date divider in a list.
        titleSmall = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Use Case: Long-form text where readability is key.
        // Example: The main content of an article or a detailed description.
        bodyLarge = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),

        // Use Case: The default text style for most content.
        // Example: The subtitle in a TopAppBar or the secondary text in a list item.
        bodyMedium = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),

        // Use Case: The smallest body text, for supplementary information.
        // Example: Helper text below a text field, image captions, or timestamps.
        bodySmall = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),

        // Use Case: For interactive elements that need high emphasis.
        // Example: The text inside a Button, TextButton, or OutlinedButton.
        labelLarge = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Use Case: The default label style, for medium-emphasis text.
        // Example: The label of a TextField or the text for a tab.
        labelMedium = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),

        // Use Case: The smallest label style, for low-emphasis text.
        // Example: Overline text or small icon labels.
        labelSmall = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}