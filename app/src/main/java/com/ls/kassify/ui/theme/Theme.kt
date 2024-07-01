package com.ls.kassify.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

internal val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFedea1a),
    secondary = Color(0xFFedea1a),
    tertiary = Color(0xFFedea1a)
)

internal val LightColorScheme = lightColorScheme(
    primary = Blue,
    background = White,
    tertiary = LightBlue,
    surface = Green,
    error= Red,
    secondary = MiddleBlue,
    onBackground = Black,
    outline=Black,
    primaryContainer = Grey,
    outlineVariant = White)

@Composable
fun KassifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}