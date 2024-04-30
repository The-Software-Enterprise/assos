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

private val DarkColorScheme =
    darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)

private val LightColorScheme =
    lightColorScheme(
        primary = NeonBlue,
        onPrimary = MrWhite,
        primaryContainer = MrWhite,
        inversePrimary = JordyBlue,
        secondary = CornflowerBlue,
        onSecondary = MrWhite,
        secondaryContainer = MrWhite,
        tertiary = JordyBlue,
        onTertiary = MrWhite,
        tertiaryContainer = MrWhite,
        onTertiaryContainer = JordyBlue,
        background = MrWhite,
        onBackground = MrBlack,
        surface = LightCyan,
        onSurface = MrBlack,
        surfaceVariant = GreySpector,
        onSurfaceVariant = MrBlack,
        surfaceTint = Periwinkle,
        inverseSurface = MrBlack,
        inverseOnSurface = MrBlack,
        error = ErrorRed,
        onError = MrWhite,
        errorContainer = ErrorBackround,
        onErrorContainer = MrBlack,
        outline = GraySeparator,
        outlineVariant = Periwinkle
    )

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
