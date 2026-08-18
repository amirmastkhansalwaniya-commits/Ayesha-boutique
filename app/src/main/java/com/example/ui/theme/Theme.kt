package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.example.data.model.AppFontPreset
import com.example.data.model.AppThemePreset

data class BoutiqueColors(
    val primary: Color,
    val darkBackground: Color,
    val cardBackground: Color,
    val surfaceColor: Color,
    val accentColor: Color,
    val textColor: Color,
    val textMutedColor: Color,
    val borderGold: Color,
    val brightGold: Color
)

val LocalBoutiqueColors = staticCompositionLocalOf {
    BoutiqueColors(
        primary = GoldPrimary,
        darkBackground = MaroonDarkest,
        cardBackground = MaroonCard,
        surfaceColor = MaroonSurface,
        accentColor = MaroonAccent,
        textColor = ChampagneSilk,
        textMutedColor = ChampagneMuted,
        borderGold = GoldBorder,
        brightGold = GoldBright
    )
}

val LocalBoutiqueFont = staticCompositionLocalOf<FontFamily> { FontFamily.Serif }

@Composable
fun MyApplicationTheme(
    customTheme: AppThemePreset = AppThemePreset.MAROON_GOLD,
    customFont: AppFontPreset = AppFontPreset.SERIF,
    content: @Composable () -> Unit
) {
    val boutiqueColors = BoutiqueColors(
        primary = customTheme.primaryColor,
        darkBackground = customTheme.darkBackground,
        cardBackground = customTheme.cardBackground,
        surfaceColor = customTheme.surfaceColor,
        accentColor = customTheme.accentColor,
        textColor = customTheme.textColor,
        textMutedColor = customTheme.textColor.copy(alpha = 0.65f),
        borderGold = customTheme.primaryColor.copy(alpha = 0.45f),
        brightGold = customTheme.primaryColor
    )

    val colorScheme = darkColorScheme(
        primary = customTheme.primaryColor,
        onPrimary = customTheme.darkBackground,
        primaryContainer = customTheme.surfaceColor,
        onPrimaryContainer = customTheme.textColor,
        secondary = customTheme.primaryColor.copy(alpha = 0.8f),
        onSecondary = customTheme.darkBackground,
        secondaryContainer = customTheme.cardBackground,
        onSecondaryContainer = customTheme.textColor,
        tertiary = customTheme.accentColor,
        onTertiary = Color.White,
        background = customTheme.darkBackground,
        onBackground = customTheme.textColor,
        surface = customTheme.surfaceColor,
        onSurface = customTheme.textColor,
        surfaceVariant = customTheme.cardBackground,
        onSurfaceVariant = customTheme.textColor.copy(alpha = 0.8f),
        outline = customTheme.primaryColor.copy(alpha = 0.45f),
        outlineVariant = customTheme.accentColor,
        error = CrimsonUrgent,
        onError = Color.White
    )

    CompositionLocalProvider(
        LocalBoutiqueColors provides boutiqueColors,
        LocalBoutiqueFont provides customFont.fontFamily
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
