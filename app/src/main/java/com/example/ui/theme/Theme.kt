package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TikTokMagenta,
    secondary = TikTokCyan,
    tertiary = CoinGold,
    background = DarkBackground,
    surface = TikTokBlack,
    surfaceVariant = TikTokSurface,
    onPrimary = TextWhite,
    onSecondary = TikTokBlack,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun TokTokTheme(
    darkTheme: Boolean = true, // Default to dark video UI like TikTok
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

