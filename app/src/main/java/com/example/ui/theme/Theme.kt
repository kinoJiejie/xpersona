package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Define ColorAccentRed inline if we need it
private val ColorAccentRed = androidx.compose.ui.graphics.Color(0xFFEF4444)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryThemeColor,
    secondary = TextSecondary,
    tertiary = AccentGoldVIP,
    background = ThemeBackgroundDeep,
    surface = ThemeCardBgDark,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ColorAccentRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark theme by default
    content: @Composable () -> Unit,
) {
    // We enforce our specific premium dark palette for Xpersona design brand identity
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = XpersonaTypography,
        content = content
    )
}

