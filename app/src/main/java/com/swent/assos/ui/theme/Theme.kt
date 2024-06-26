package com.swent.assos.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme =
    lightColorScheme(
        primary = NeonBlue, // Important buttons such as Join, Apply...
        onPrimary = MrWhite,
        primaryContainer = MrWhite,
        inversePrimary = JordyBlue,
        secondary = CornflowerBlue, // Buttons such as Follow...
        onSecondary = MrWhite,
        secondaryContainer = BackgroundSecondary,
        tertiary = JordyBlue, // Floating action button such as plus...
        onTertiary = MrWhite,
        tertiaryContainer = MrWhite,
        onTertiaryContainer = JordyBlue,
        background = MrWhite,
        onBackground = MrBlack,
        surface = surfaceColor, // Background of a component
        onSurface = MrBlack,
        surfaceVariant = surface, // Darker background of a component
        onSurfaceVariant = MrBlack,
        surfaceTint = Periwinkle,
        inverseSurface = MrBlack,
        inverseOnSurface = MrBlack,
        error = ErrorRed,
        onError = MrWhite,
        errorContainer = ErrorBackground,
        onErrorContainer = MrBlack,
        outline = GraySeparator,
        outlineVariant = MrWhite)

private val DarkColorScheme =
    darkColorScheme(
        primary = NeonBlue,
        onPrimary = MrWhite,
        primaryContainer = MrBlack,
        inversePrimary = NeonBlue,
        secondary = CornflowerBlue,
        onSecondary = MrWhite,
        secondaryContainer = CornflowerBlue,
        tertiary = JordyBlue,
        onTertiary = MrWhite,
        tertiaryContainer = Periwinkle,
        onTertiaryContainer = JordyBlue,
        background = MrBlack,
        onBackground = MrWhite,
        surface = surfaceColor,
        onSurface = MrBlack,
        surfaceVariant = surfaceColor2,
        onSurfaceVariant = MrWhite,
        surfaceTint = CornflowerBlue,
        inverseSurface = MrWhite,
        inverseOnSurface = MrBlack,
        error = ErrorRed,
        onError = MrWhite,
        errorContainer = ErrorBackground,
        onErrorContainer = MrBlack,
        outline = GreySpector,
        outlineVariant = MrBlack)

@Composable
fun AssosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
  val colorScheme =
      when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
      }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = colorScheme.background.toArgb()
      WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
