package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AppFontPreset
import com.example.data.model.AppLogoPreset
import com.example.data.model.AppThemePreset
import com.example.data.model.CustomAppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("aysha_boutique_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<CustomAppSettings> = _settings.asStateFlow()

    private fun loadSettings(): CustomAppSettings {
        val name = prefs.getString("boutique_name", "Aysha Boutique") ?: "Aysha Boutique"
        val tagline = prefs.getString("boutique_tagline", "Bespoke Couture & Tailoring Atelier") ?: "Bespoke Couture & Tailoring Atelier"
        val currency = prefs.getString("currency_symbol", "₹") ?: "₹"

        val themeId = prefs.getString("theme_id", AppThemePreset.MAROON_GOLD.id)
        val theme = AppThemePreset.values().find { it.id == themeId } ?: AppThemePreset.MAROON_GOLD

        val fontId = prefs.getString("font_id", AppFontPreset.SERIF.id)
        val font = AppFontPreset.values().find { it.id == fontId } ?: AppFontPreset.SERIF

        val logoId = prefs.getString("logo_id", AppLogoPreset.ROYAL_SHEARS.id)
        val logo = AppLogoPreset.values().find { it.id == logoId } ?: AppLogoPreset.ROYAL_SHEARS

        val customLogoPath = prefs.getString("custom_logo_path", null)

        return CustomAppSettings(
            boutiqueName = name,
            boutiqueTagline = tagline,
            currencySymbol = currency,
            selectedTheme = theme,
            selectedFont = font,
            selectedLogo = logo,
            customLogoPath = customLogoPath,
            creatorCredit = "This app created by Amir Khan"
        )
    }

    fun updateSettings(newSettings: CustomAppSettings) {
        prefs.edit()
            .putString("boutique_name", newSettings.boutiqueName)
            .putString("boutique_tagline", newSettings.boutiqueTagline)
            .putString("currency_symbol", newSettings.currencySymbol)
            .putString("theme_id", newSettings.selectedTheme.id)
            .putString("font_id", newSettings.selectedFont.id)
            .putString("logo_id", newSettings.selectedLogo.id)
            .putString("custom_logo_path", newSettings.customLogoPath)
            .apply()

        _settings.value = newSettings
    }

    fun setTheme(theme: AppThemePreset) {
        updateSettings(_settings.value.copy(selectedTheme = theme))
    }

    fun setFont(font: AppFontPreset) {
        updateSettings(_settings.value.copy(selectedFont = font))
    }

    fun setLogo(logo: AppLogoPreset) {
        updateSettings(_settings.value.copy(selectedLogo = logo, customLogoPath = null))
    }

    fun setCustomLogoPath(path: String?) {
        updateSettings(_settings.value.copy(customLogoPath = path))
    }

    fun setBoutiqueDetails(name: String, tagline: String) {
        updateSettings(_settings.value.copy(boutiqueName = name, boutiqueTagline = tagline))
    }

    fun setCurrency(currency: String) {
        updateSettings(_settings.value.copy(currencySymbol = currency))
    }
}
