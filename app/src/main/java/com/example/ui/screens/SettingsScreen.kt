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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.AppStylePreset
import com.example.data.model.AppThemePreset
import com.example.data.model.BoutiqueOrder
import com.example.data.model.CustomAppSettings
import com.example.data.model.PrecisionCategory
import com.example.data.model.PrecisionMeasurementCatalog
import com.example.data.model.PrecisionMeasurementOption
import com.example.ui.components.BoutiqueLogoView
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont
import com.example.util.PdfReportGenerator
import java.io.File

@Composable
fun SettingsScreen(
    settings: CustomAppSettings,
    orders: List<BoutiqueOrder> = emptyList(),
    customMeasurementPresets: List<PrecisionMeasurementOption> = emptyList(),
    onUpdateTheme: (AppThemePreset) -> Unit,
    onUpdateFont: (AppFontPreset) -> Unit,
    onUpdateFontSize: (com.example.data.model.AppFontSizePreset) -> Unit = {},
    onUpdateStyle: (AppStylePreset) -> Unit,
    onUpdateLogo: (AppLogoPreset) -> Unit,
    onUpdateCustomLogoPath: (String?) -> Unit,
    onUpdateBoutiqueName: (String, String) -> Unit,
    onUpdateCurrency: (String) -> Unit = {},
    onAddCustomPreset: (PrecisionMeasurementOption) -> Unit = {},
    onDeleteCustomPreset: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    var boutiqueNameText by remember(settings.boutiqueName) { mutableStateOf(settings.boutiqueName) }
    var boutiqueTaglineText by remember(settings.boutiqueTagline) { mutableStateOf(settings.boutiqueTagline) }
    var isNameSavedFeedback by remember { mutableStateOf(false) }

    var showCreateMasterPresetDialog by remember { mutableStateOf(false) }
    var newPresetName by remember { mutableStateOf("") }
    var newPresetHindiName by remember { mutableStateOf("") }
    var newPresetCategory by remember { mutableStateOf(PrecisionCategory.UPPER_BODY) }
    var newPresetDescription by remember { mutableStateOf("") }
    var selectedCatalogCategory by remember { mutableStateOf(PrecisionCategory.ALL) }

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
                Toast.makeText(context, "Logo updated successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.darkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Title & Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoutiqueLogoView(
                        preset = settings.selectedLogo,
                        customPath = settings.customLogoPath,
                        primaryColor = colors.brightGold,
                        surfaceColor = colors.surfaceColor,
                        size = 54.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = settings.boutiqueName,
                            color = colors.brightGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont
                        )
                        Text(
                            text = settings.boutiqueTagline,
                            color = colors.textColor.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Currency: ₹ (Rupee)",
                                color = colors.brightGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // 1. PDF EXPORT & REPORT SECTION (ONLY PDF)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PDF Export & Statements (केवल PDF रिपोर्ट)",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Generate and download official PDF order sheets",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "सभी ऑर्डर्स, पेमेंट्स और मेजरमेंट की रिपोर्ट केवल PDF फॉर्मेट में एक्सपोर्ट करें:",
                        color = colors.textColor,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val pdfFile = PdfReportGenerator.generateOrdersReportPdf(context, orders, settings)
                            if (pdfFile != null) {
                                PdfReportGenerator.openPdfDirectly(context, pdfFile)
                                Toast.makeText(context, "PDF सफलतापूर्वक खुल गई!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "PDF तैयार करने में त्रुटि", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.brightGold,
                            contentColor = colors.darkBackground
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("export_pdf_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Download PDF Statement / Report (PDF डाउनलोड)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // 2. THEME & COLOR PALETTES
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Theme & Color Customization (रंग व थीम)",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Select luxury royal palette presets",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    AppThemePreset.values().forEach { preset ->
                        val isSelected = settings.selectedTheme == preset
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) colors.surfaceColor else Color.Transparent)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onUpdateTheme(preset) }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Color sample dot
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(preset.primaryColor)
                                            .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = preset.title,
                                            color = if (isSelected) colors.brightGold else colors.textColor,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = preset.description,
                                            color = colors.textMutedColor,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(colors.brightGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = colors.darkBackground,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. FONT & TYPOGRAPHY CUSTOMIZATION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FontDownload,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Font & Typography Style (फ़ॉन्ट शैली)",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Choose boutique typography styling",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    AppFontPreset.values().forEach { preset ->
                        val isSelected = settings.selectedFont == preset
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) colors.surfaceColor else Color.Transparent)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onUpdateFont(preset) }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = preset.title,
                                        color = if (isSelected) colors.brightGold else colors.textColor,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp,
                                        fontFamily = preset.fontFamily
                                    )
                                    Text(
                                        text = preset.description,
                                        color = colors.textMutedColor,
                                        fontSize = 11.sp
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(colors.brightGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = colors.darkBackground,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Font Size Options
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FontDownload,
                            contentDescription = null,
                            tint = colors.brightGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "App Font Size (अक्षरों का साइज़)",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    com.example.data.model.AppFontSizePreset.values().forEach { sizePreset ->
                        val isSizeSelected = settings.selectedFontSize == sizePreset
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSizeSelected) colors.surfaceColor else Color.Transparent)
                                .border(
                                    width = if (isSizeSelected) 1.5.dp else 1.dp,
                                    color = if (isSizeSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onUpdateFontSize(sizePreset) }
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = sizePreset.title,
                                        color = if (isSizeSelected) colors.brightGold else colors.textColor,
                                        fontWeight = if (isSizeSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = sizePreset.description,
                                        color = colors.textMutedColor,
                                        fontSize = 10.5.sp
                                    )
                                }

                                if (isSizeSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(colors.brightGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = colors.darkBackground,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. STYLE & LAYOUT CUSTOMIZATION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Style,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "App Visual Style Preset (स्टाइल व लेआउट)",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Configure card curvature & luxury finishes",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    AppStylePreset.values().forEach { preset ->
                        val isSelected = settings.selectedStyle == preset
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) colors.surfaceColor else Color.Transparent)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onUpdateStyle(preset) }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = preset.title,
                                        color = if (isSelected) colors.brightGold else colors.textColor,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = preset.description,
                                        color = colors.textMutedColor,
                                        fontSize = 11.sp
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(colors.brightGold),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = colors.darkBackground,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. ATELIER IDENTITY & BRANDING
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Boutique Atelier Identity",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Configure shop name & receipt headers",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = boutiqueNameText,
                        onValueChange = { boutiqueNameText = it },
                        label = { Text("Boutique / Shop Name", color = colors.textMutedColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.brightGold,
                            unfocusedBorderColor = colors.borderGold.copy(alpha = 0.5f),
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor,
                            cursorColor = colors.brightGold
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = boutiqueTaglineText,
                        onValueChange = { boutiqueTaglineText = it },
                        label = { Text("Tagline / Subtitle", color = colors.textMutedColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.brightGold,
                            unfocusedBorderColor = colors.borderGold.copy(alpha = 0.5f),
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor,
                            cursorColor = colors.brightGold
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onUpdateBoutiqueName(boutiqueNameText, boutiqueTaglineText)
                            isNameSavedFeedback = true
                            Toast.makeText(context, "Boutique details saved!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.brightGold,
                            contentColor = colors.darkBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNameSavedFeedback) "Saved ✓" else "Save Details",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 6. LOGO & EMBLEM SELECTION
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceColor)
                                .border(1.dp, colors.brightGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = colors.brightGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Boutique Emblem & Logo",
                                color = colors.textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Choose a preset insignia or upload custom PNG",
                                color = colors.textMutedColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLogoPreset.values().take(3).forEach { preset ->
                            val isSelected = settings.selectedLogo == preset && settings.customLogoPath == null
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) colors.surfaceColor else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onUpdateLogo(preset) }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(preset.symbol, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        preset.title.take(10),
                                        color = if (isSelected) colors.brightGold else colors.textColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLogoPreset.values().drop(3).forEach { preset ->
                            val isSelected = settings.selectedLogo == preset && settings.customLogoPath == null
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) colors.surfaceColor else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onUpdateLogo(preset) }
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(preset.symbol, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        preset.title.take(10),
                                        color = if (isSelected) colors.brightGold else colors.textColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textColor),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (settings.customLogoPath != null) "Change Uploaded Logo" else "Upload Custom Logo (PNG/JPG)",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 7. PRECISION MEASUREMENT OPTIONS CATALOG & MASTER PRESETS
        item {
            val totalSystemOptions = PrecisionMeasurementCatalog.defaultOptions.size
            val totalCustomOptions = customMeasurementPresets.size
            val allCombinedOptions = remember(customMeasurementPresets) {
                PrecisionMeasurementCatalog.defaultOptions + customMeasurementPresets
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(colors.surfaceColor)
                                    .border(1.dp, colors.brightGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📏", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Precision Measurement Catalog",
                                    color = colors.textColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$totalSystemOptions Built-in + $totalCustomOptions Custom Options",
                                    color = colors.brightGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Button(
                            onClick = {
                                newPresetName = ""
                                newPresetHindiName = ""
                                newPresetCategory = PrecisionCategory.UPPER_BODY
                                newPresetDescription = ""
                                showCreateMasterPresetDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.brightGold,
                                contentColor = colors.darkBackground
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+ New Option", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Browse options available in Client & Order creation dialogs:",
                        color = colors.textMutedColor,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Category Filter Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            PrecisionCategory.ALL to "All",
                            PrecisionCategory.UPPER_BODY to "Upper",
                            PrecisionCategory.SLEEVE_COLLAR to "Neck/Sleeve",
                            PrecisionCategory.LOWER_BODY to "Bottoms",
                            PrecisionCategory.CUSTOM to "Custom"
                        ).forEach { (cat, label) ->
                            val isSelected = selectedCatalogCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) colors.brightGold else colors.surfaceColor)
                                    .border(
                                        0.8.dp,
                                        if (isSelected) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedCatalogCategory = cat }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) colors.darkBackground else colors.textColor,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Options List
                    val filteredCatalog = allCombinedOptions.filter {
                        if (selectedCatalogCategory == PrecisionCategory.ALL) true
                        else if (selectedCatalogCategory == PrecisionCategory.CUSTOM) !it.isSystemPreset
                        else it.category == selectedCatalogCategory
                    }

                    if (filteredCatalog.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.surfaceColor.copy(alpha = 0.5f))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedCatalogCategory == PrecisionCategory.CUSTOM)
                                    "No custom master options added yet. Tap '+ New Option' above to define atelier-specific measurements!"
                                else "No options found in this category.",
                                color = colors.textMutedColor,
                                fontSize = 11.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.heightIn(max = 280.dp).verticalScroll(rememberScrollState())
                        ) {
                            filteredCatalog.forEach { opt ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colors.surfaceColor)
                                        .border(0.5.dp, colors.borderGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = opt.name,
                                                    color = colors.textColor,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                if (opt.hindiName.isNotBlank()) {
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "(${opt.hindiName})",
                                                        color = colors.brightGold,
                                                        fontSize = 11.sp
                                                    )
                                                }
                                                if (!opt.isSystemPreset) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(colors.brightGold.copy(alpha = 0.2f))
                                                            .padding(horizontal = 4.dp, vertical = 1.dp)
                                                    ) {
                                                        Text("Custom", color = colors.brightGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                            if (opt.description.isNotBlank()) {
                                                Text(
                                                    text = opt.description,
                                                    color = colors.textMutedColor,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }

                                        if (!opt.isSystemPreset) {
                                            IconButton(
                                                onClick = {
                                                    onDeleteCustomPreset(opt.id)
                                                    Toast.makeText(context, "Option removed from master catalog", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Delete Option",
                                                    tint = Color(0xFFF87171),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // MANDATORY CREATOR FOOTER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceColor)
                    .border(1.dp, colors.borderGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Aysha Boutique Management System",
                        color = colors.brightGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "This app created by Amir Khan",
                        color = colors.textColor.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Create Master Preset Dialog
    if (showCreateMasterPresetDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showCreateMasterPresetDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.darkBackground),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.borderGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NEW MASTER MEASUREMENT OPTION",
                            color = colors.brightGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont
                        )
                        IconButton(
                            onClick = { showCreateMasterPresetDialog = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Close", tint = colors.textMutedColor, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = newPresetName,
                        onValueChange = { newPresetName = it },
                        label = { Text("Measurement Name (English) *", color = colors.textMutedColor) },
                        placeholder = { Text("e.g. Front Cross, Neck Width", color = colors.textMutedColor.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.brightGold,
                            unfocusedBorderColor = colors.borderGold.copy(alpha = 0.4f),
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPresetHindiName,
                        onValueChange = { newPresetHindiName = it },
                        label = { Text("Hindi / Atelier Term (हिंदी नाम)", color = colors.textMutedColor) },
                        placeholder = { Text("e.g. आगे का तीरा, कॉलर चौड़ाई", color = colors.textMutedColor.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.brightGold,
                            unfocusedBorderColor = colors.borderGold.copy(alpha = 0.4f),
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPresetDescription,
                        onValueChange = { newPresetDescription = it },
                        label = { Text("Tailor Guidelines / Description", color = colors.textMutedColor) },
                        placeholder = { Text("How to measure this point accurately", color = colors.textMutedColor.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surfaceColor,
                            unfocusedContainerColor = colors.surfaceColor,
                            focusedBorderColor = colors.brightGold,
                            unfocusedBorderColor = colors.borderGold.copy(alpha = 0.4f),
                            focusedTextColor = colors.textColor,
                            unfocusedTextColor = colors.textColor
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { showCreateMasterPresetDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textMutedColor)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (newPresetName.isNotBlank()) {
                                    val newOpt = PrecisionMeasurementOption(
                                        id = "custom_${System.currentTimeMillis()}",
                                        name = newPresetName.trim(),
                                        hindiName = newPresetHindiName.trim(),
                                        category = PrecisionCategory.CUSTOM,
                                        description = newPresetDescription.trim(),
                                        isSystemPreset = false
                                    )
                                    onAddCustomPreset(newOpt)
                                    Toast.makeText(context, "Added to Precision Catalog!", Toast.LENGTH_SHORT).show()
                                    showCreateMasterPresetDialog = false
                                }
                            },
                            enabled = newPresetName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.brightGold,
                                contentColor = colors.darkBackground
                            )
                        ) {
                            Text("Save Master Preset", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
