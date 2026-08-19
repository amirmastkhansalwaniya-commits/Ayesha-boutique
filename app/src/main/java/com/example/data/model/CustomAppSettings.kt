package com.example.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

enum class AppThemePreset(
    val id: String,
    val title: String,
    val description: String,
    val primaryColor: Color,
    val darkBackground: Color,
    val cardBackground: Color,
    val surfaceColor: Color,
    val accentColor: Color,
    val textColor: Color
) {
    MAROON_GOLD(
        id = "maroon_gold",
        title = "Royal Maroon & Gold (शाही मरून)",
        description = "Deep burgundy velvet with 24k gold accents",
        primaryColor = Color(0xFFD4AF37),
        darkBackground = Color(0xFF190306),
        cardBackground = Color(0xFF3B0B16),
        surfaceColor = Color(0xFF330912),
        accentColor = Color(0xFF6B1425),
        textColor = Color(0xFFFFF7EB)
    ),
    EMERALD_GOLD(
        id = "emerald_gold",
        title = "Imperial Emerald & Gold (शाही पन्ना)",
        description = "Deep forest emerald with gleaming gold",
        primaryColor = Color(0xFFE5C158),
        darkBackground = Color(0xFF03140C),
        cardBackground = Color(0xFF0B291A),
        surfaceColor = Color(0xFF082115),
        accentColor = Color(0xFF154C32),
        textColor = Color(0xFFF1FAF4)
    ),
    SAPPHIRE_CHAMPAGNE(
        id = "sapphire_champagne",
        title = "Royal Sapphire & Champagne (शाही नीलम)",
        description = "Midnight oceanic blue with champagne sheen",
        primaryColor = Color(0xFFDFB86C),
        darkBackground = Color(0xFF030B18),
        cardBackground = Color(0xFF0C1D38),
        surfaceColor = Color(0xFF08152A),
        accentColor = Color(0xFF14305C),
        textColor = Color(0xFFF0F5FF)
    ),
    OBSIDIAN_ROSE(
        id = "obsidian_rose",
        title = "Obsidian & Rose Gold (चारकोल रोज)",
        description = "Pure midnight black with luminous rose gold",
        primaryColor = Color(0xFFE8A598),
        darkBackground = Color(0xFF0D0D11),
        cardBackground = Color(0xFF1B1B22),
        surfaceColor = Color(0xFF15151B),
        accentColor = Color(0xFF352B30),
        textColor = Color(0xFFFFF0ED)
    ),
    PLUM_GOLD(
        id = "plum_gold",
        title = "Velvet Plum & Pale Gold (शाही जामुनी)",
        description = "Aristocratic deep purple with warm honey gold",
        primaryColor = Color(0xFFECC272),
        darkBackground = Color(0xFF140416),
        cardBackground = Color(0xFF2E0C32),
        surfaceColor = Color(0xFF240927),
        accentColor = Color(0xFF55175D),
        textColor = Color(0xFFFBF4FD)
    )
}

enum class AppFontSizePreset(
    val id: String,
    val title: String,
    val scaleFactor: Float,
    val description: String
) {
    COMPACT(
        id = "compact",
        title = "Compact (छोटा - 85%)",
        scaleFactor = 0.88f,
        description = "Smaller compact text for dense screen viewing"
    ),
    DEFAULT(
        id = "default",
        title = "Standard (सामान्य - 100%)",
        scaleFactor = 1.0f,
        description = "Standard balanced typography size"
    ),
    COMFORTABLE(
        id = "comfortable",
        title = "Comfortable (बड़ा - 115%)",
        scaleFactor = 1.15f,
        description = "Enhanced clarity and enlarged comfortable reading"
    ),
    EXTRA_LARGE(
        id = "extra_large",
        title = "Extra Large (विशाल - 130%)",
        scaleFactor = 1.30f,
        description = "Maximum size for effortless legibility"
    )
}

enum class AppStylePreset(
    val id: String,
    val title: String,
    val description: String,
    val cornerRadius: Int = 16
) {
    ROYAL_LUXE(
        id = "royal_luxe",
        title = "Royal Classic Luxe (शाही लक्ज़री)",
        description = "Gold filigree borders with 18dp luxury curves",
        cornerRadius = 18
    ),
    MODERN_MINIMAL(
        id = "modern_minimal",
        title = "Modern Atelier Minimal (मॉडर्न मिनिमल)",
        description = "Clean geometric borders with high-contrast polish",
        cornerRadius = 10
    ),
    HAUTE_COUTURE(
        id = "haute_couture",
        title = "Haute Couture Glass (हाउत कूटूर)",
        description = "Soft translucent glass curves with ambient glow",
        cornerRadius = 24
    )
}

enum class AppFontPreset(
    val id: String,
    val title: String,
    val sampleText: String,
    val fontFamily: FontFamily
) {
    SERIF(
        id = "serif",
        title = "Royal Serif (Elegant)",
        sampleText = "Aysha Couture Bespoke",
        fontFamily = FontFamily.Serif
    ),
    SANS_SERIF(
        id = "sans_serif",
        title = "Modern Clean (Sans)",
        sampleText = "Aysha Couture Bespoke",
        fontFamily = FontFamily.SansSerif
    ),
    CURSIVE(
        id = "cursive",
        title = "Signature Script (Cursive)",
        sampleText = "Aysha Couture Bespoke",
        fontFamily = FontFamily.Cursive
    ),
    MONOSPACE(
        id = "monospace",
        title = "Technical Tailor (Mono)",
        sampleText = "Aysha Couture Bespoke",
        fontFamily = FontFamily.Monospace
    )
}

enum class AppLogoPreset(
    val id: String,
    val title: String,
    val symbol: String
) {
    ROYAL_SHEARS("shears", "Royal Shears & Monogram", "✂️"),
    GOLDEN_NEEDLE("needle", "Golden Needle & Thread", "🪡"),
    BOUTIQUE_CROWN("crown", "Imperial Boutique Crown", "👑"),
    SEWING_MACHINE("machine", "Vintage Sewing Machine", "🧵"),
    COUTURE_DRESS("dress", "Haute Couture Gown", "👗"),
    DIAMOND_EMBLEM("diamond", "Prestige Diamond Crest", "💎")
}

data class CustomAppSettings(
    val boutiqueName: String = "Aysha Boutique",
    val boutiqueTagline: String = "Bespoke Couture & Tailoring Atelier",
    val currencySymbol: String = "₹",
    val selectedTheme: AppThemePreset = AppThemePreset.MAROON_GOLD,
    val selectedFont: AppFontPreset = AppFontPreset.SERIF,
    val selectedFontSize: AppFontSizePreset = AppFontSizePreset.DEFAULT,
    val selectedStyle: AppStylePreset = AppStylePreset.ROYAL_LUXE,
    val selectedLogo: AppLogoPreset = AppLogoPreset.ROYAL_SHEARS,
    val customLogoPath: String? = null,
    val creatorCredit: String = "This app created by Amir Khan"
)

