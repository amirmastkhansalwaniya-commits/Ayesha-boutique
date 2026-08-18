package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppFontPreset
import com.example.data.model.AppLogoPreset
import com.example.data.model.AppThemePreset
import com.example.data.model.CustomAppSettings
import com.example.ui.components.BoutiqueLogoView
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont
import java.io.File

@Composable
fun SettingsScreen(
    settings: CustomAppSettings,
    onUpdateTheme: (AppThemePreset) -> Unit,
    onUpdateFont: (AppFontPreset) -> Unit,
    onUpdateLogo: (AppLogoPreset) -> Unit,
    onUpdateCustomLogoPath: (String?) -> Unit,
    onUpdateBoutiqueName: (String, String) -> Unit,
    onUpdateCurrency: (String) -> Unit
) {
    val context = LocalContext.current
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    var boutiqueNameText by remember(settings.boutiqueName) { mutableStateOf(settings.boutiqueName) }
    var boutiqueTaglineText by remember(settings.boutiqueTagline) { mutableStateOf(settings.boutiqueTagline) }
    var isNameSavedFeedback by remember { mutableStateOf(false) }

    // Gallery Image Picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val targetFile = File(context.filesDir, "custom_boutique_logo.png")
                inputStream?.use { input ->
                    targetFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                onUpdateCustomLogoPath(targetFile.absolutePath)
                Toast.makeText(context, "लोगो गैलरी से सफलतापूर्वक सेट किया गया!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "फोटो लोड करने में त्रुटि हुई", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val currencyOptions = listOf("₹", "$", "AED", "SAR", "£", "€")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.darkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Live Preview Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(colors.accentColor.copy(alpha = 0.5f), colors.cardBackground))
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            BoutiqueLogoView(
                                customLogoPath = settings.customLogoPath,
                                logoSymbol = settings.selectedLogo.symbol,
                                size = 52.dp,
                                fontSize = 24.dp,
                                borderColor = colors.primary
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = settings.boutiqueName,
                                    color = colors.textColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = currentFont
                                )
                                Text(
                                    text = settings.boutiqueTagline,
                                    color = colors.brightGold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.primary.copy(alpha = 0.2f))
                                .border(1.dp, colors.primary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "मुद्रा: ${settings.currencySymbol}",
                                color = colors.brightGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Gallery Image / Custom Logo Upload (गैलरी से फोटो / लोगो अपलोड)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.borderGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APP LOGO & PHOTO (गैलरी से फोटो / लोगो)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "अपनी दुकान का लोगो या कोई भी फोटो अपने फोन की गैलरी से चुनें, जो ऐप के हेडर में दिखाई देगी:",
                        color = colors.textMutedColor,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BoutiqueLogoView(
                            customLogoPath = settings.customLogoPath,
                            logoSymbol = settings.selectedLogo.symbol,
                            size = 60.dp,
                            fontSize = 28.dp,
                            borderColor = colors.brightGold,
                            borderWidth = 2.dp
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Button(
                                onClick = { galleryLauncher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.darkBackground),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().testTag("pick_gallery_image_button")
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("गैलरी से फोटो चुनें (Pick from Gallery)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            if (!settings.customLogoPath.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedButton(
                                    onClick = {
                                        onUpdateCustomLogoPath(null)
                                        Toast.makeText(context, "कस्टम फोटो हटा दी गई है", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textColor),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("फोटो हटाएं (Use Icon)", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "या नीचे दिए गए प्रतीक चिह्नों (Icons) में से चुनें:",
                        color = colors.textMutedColor,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLogoPreset.values().forEach { logo ->
                            val isSelected = settings.customLogoPath == null && settings.selectedLogo == logo
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) colors.surfaceColor else colors.darkBackground)
                                    .border(
                                        if (isSelected) 1.5.dp else 0.8.dp,
                                        if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        onUpdateLogo(logo)
                                        Toast.makeText(context, "लोगो बदला गया: ${logo.symbol}", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = logo.symbol, fontSize = 22.sp)
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Icon(Icons.Default.Check, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: App & Boutique Name (ऐप का नाम व टैगलाइन)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.borderGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storefront, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APP & BOUTIQUE NAME (ऐप का नाम)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = boutiqueNameText,
                        onValueChange = {
                            boutiqueNameText = it
                            isNameSavedFeedback = false
                        },
                        label = { Text("Boutique / App Name", color = colors.textMutedColor) },
                        modifier = Modifier.fillMaxWidth().testTag("settings_boutique_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.borderGold,
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor,
                            cursorColor = colors.brightGold
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = boutiqueTaglineText,
                        onValueChange = {
                            boutiqueTaglineText = it
                            isNameSavedFeedback = false
                        },
                        label = { Text("Atelier Tagline / Subtitle", color = colors.textMutedColor) },
                        modifier = Modifier.fillMaxWidth().testTag("settings_boutique_tagline_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.primary,
                            unfocusedBorderColor = colors.borderGold,
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor,
                            cursorColor = colors.brightGold
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (boutiqueNameText.isNotBlank()) {
                                onUpdateBoutiqueName(boutiqueNameText.trim(), boutiqueTaglineText.trim())
                                isNameSavedFeedback = true
                                Toast.makeText(context, "नाम सफलतापूर्वक अपडेट हो गया!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.darkBackground),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.End).testTag("settings_save_name_button")
                    ) {
                        Icon(if (isNameSavedFeedback) Icons.Default.Check else Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isNameSavedFeedback) "Saved!" else "Update Name (सेव करें)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Section 3: Luxury Theme & Color Palette (थीम व कलर)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.borderGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APP THEME & COLORS (थीम व रंग)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AppThemePreset.values().forEach { theme ->
                        val isSelected = settings.selectedTheme == theme
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) colors.surfaceColor else colors.darkBackground)
                                .border(
                                    if (isSelected) 1.5.dp else 0.8.dp,
                                    if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onUpdateTheme(theme)
                                    Toast.makeText(context, "थीम लागू की गई: ${theme.title}", Toast.LENGTH_SHORT).show()
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Color swatch bubbles
                                    Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(theme.primaryColor)
                                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(theme.darkBackground)
                                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(theme.accentColor)
                                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = theme.title,
                                            color = if (isSelected) colors.brightGold else colors.textColor,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = theme.description,
                                            color = colors.textMutedColor,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = colors.brightGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 4: Typography & Fonts (फॉन्ट स्टाइल)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.borderGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FontDownload, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APP FONT & TYPOGRAPHY (फॉन्ट स्टाइल)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AppFontPreset.values().forEach { font ->
                        val isSelected = settings.selectedFont == font
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) colors.surfaceColor else colors.darkBackground)
                                .border(
                                    if (isSelected) 1.5.dp else 0.8.dp,
                                    if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onUpdateFont(font)
                                    Toast.makeText(context, "फॉन्ट बदला गया: ${font.title}", Toast.LENGTH_SHORT).show()
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = font.title,
                                        color = if (isSelected) colors.brightGold else colors.textColor,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = font.sampleText,
                                        color = colors.brightGold,
                                        fontSize = 15.sp,
                                        fontFamily = font.fontFamily,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = colors.brightGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 5: Currency Symbol Selection (करेंसी - ₹ Default)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.borderGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CURRENCY SYMBOL (मुद्रा का निशान)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        currencyOptions.forEach { cur ->
                            val isSelected = settings.currencySymbol == cur
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) colors.primary else colors.darkBackground)
                                    .border(
                                        1.dp,
                                        if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.4f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        onUpdateCurrency(cur)
                                        Toast.makeText(context, "करेंसी बदली गई: $cur", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cur,
                                    color = if (isSelected) colors.darkBackground else colors.textColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // MANDATORY CREATOR FOOTER: "This app created by Amir Khan"
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colors.surfaceColor,
                                colors.darkBackground
                            )
                        )
                    )
                    .border(1.5.dp, colors.primary, RoundedCornerShape(18.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colors.brightGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OFFICIAL BOUTIQUE SOFTWARE",
                            color = colors.brightGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colors.brightGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "This app created by Amir Khan",
                        color = colors.brightGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        fontFamily = currentFont
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Crafted with bespoke precision for Aysha Boutique Atelier",
                        color = colors.textMutedColor,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(colors.cardBackground)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = EmeraldPaid,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Version 1.0.0 • Local Secure Database",
                            color = colors.textColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
