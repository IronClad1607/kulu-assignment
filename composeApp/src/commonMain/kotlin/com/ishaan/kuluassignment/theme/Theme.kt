package com.ishaan.kuluassignment.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Teal80,
    onPrimary = Teal20,
    primaryContainer = Teal30,
    onPrimaryContainer = Teal90,

    secondary = Amber80,
    onSecondary = Amber20,
    secondaryContainer = Amber30,
    onSecondaryContainer = Amber90,

    tertiary = Red80,
    onTertiary = Red20,
    tertiaryContainer = Red30,
    onTertiaryContainer = Red90,

    error = Error80,
    onError = Error20,
    errorContainer = Error30,
    onErrorContainer = Error90,

    background = DarkTheater,
    onBackground = TextOnDark,

    surface = DarkCard,
    onSurface = TextOnDark,

    surfaceVariant = DarkCard, // Can use DarkCard or a slightly different gray
    onSurfaceVariant = TextOnDarkSubtle,

    outline = DarkOutline,
    inverseSurface = Neutral90,
    inverseOnSurface = Neutral10
)

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    onPrimary = Teal100,
    primaryContainer = Teal90,
    onPrimaryContainer = Teal10,

    secondary = Amber40,
    onSecondary = Amber100,
    secondaryContainer = Amber90,
    onSecondaryContainer = Amber10,

    tertiary = Red40,
    onTertiary = Red100,
    tertiaryContainer = Red90,
    onTertiaryContainer = Red10,

    error = Error40,
    onError = Error100,
    errorContainer = Error90,
    onErrorContainer = Error10,

    background = AlmostWhite,
    onBackground = TextOnLight,

    surface = AlmostWhite,
    onSurface = TextOnLight,

    surfaceVariant = Neutral90, // A light gray for cards/dividers
    onSurfaceVariant = TextOnLightSubtle,

    outline = LightOutline,
    inverseSurface = Neutral10,
    inverseOnSurface = Neutral90
)
@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
){
    // Select color scheme based on dark theme preference
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShapes,
        typography = getAppTypography(),
        content = content
    )
}