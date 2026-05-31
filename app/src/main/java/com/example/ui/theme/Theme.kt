package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AsclGreen,
    onPrimary = AsclBlack,
    secondary = AsclGold,
    onSecondary = AsclBlack,
    tertiary = AsclGoldLight,
    onTertiary = AsclBlack,
    background = AsclBlack,
    onBackground = AsclWhite,
    surface = AsclDarkGray,
    onSurface = AsclWhite,
    surfaceVariant = AsclCardGray,
    onSurfaceVariant = AsclWhite,
    error = AsclRed
  )

private val LightColorScheme =
  darkColorScheme( // Keep dark mode aesthetic as the premium theme
    primary = AsclGreen,
    onPrimary = AsclBlack,
    secondary = AsclGold,
    onSecondary = AsclBlack,
    tertiary = AsclGoldLight,
    onTertiary = AsclBlack,
    background = AsclBlack,
    onBackground = AsclWhite,
    surface = AsclDarkGray,
    onSurface = AsclWhite,
    surfaceVariant = AsclCardGray,
    onSurfaceVariant = AsclWhite,
    error = AsclRed
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark mode aesthetic by default
  dynamicColor: Boolean = false, // Disable to preserve ASCL brand identity (Green/Gold/Black)
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
