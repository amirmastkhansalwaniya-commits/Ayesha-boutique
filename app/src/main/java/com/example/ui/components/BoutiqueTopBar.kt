package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont

@Composable
fun BoutiqueTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchPlaceholder: String = "Search orders, customers...",
    showSearch: Boolean = true,
    boutiqueName: String = "AYSHA BOUTIQUE",
    boutiqueTagline: String = "Haute Couture & Bespoke Tailoring",
    logoSymbol: String = "✂️",
    customLogoPath: String? = null,
    fontFamily: FontFamily = FontFamily.Serif,
    onOpenSettings: () -> Unit = {}
) {
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    Surface(
        color = colors.darkBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.darkBackground,
                            colors.surfaceColor
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Custom Boutique Emblem/Photo
                    BoutiqueLogoView(
                        customLogoPath = customLogoPath,
                        logoSymbol = logoSymbol,
                        size = 46.dp,
                        fontSize = 22.dp,
                        borderColor = colors.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = boutiqueName.uppercase(),
                                color = colors.brightGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.3.sp,
                                fontFamily = currentFont
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = "Luxury",
                                tint = colors.primary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = boutiqueTagline,
                            color = colors.textMutedColor,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Settings icon button with full 44dp clickable target
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceColor)
                        .border(1.2.dp, colors.primary.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .clickable { onOpenSettings() }
                        .testTag("topbar_settings_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = colors.brightGold,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            if (showSearch) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = searchPlaceholder,
                            color = colors.textMutedColor,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = colors.primary
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surfaceColor,
                        unfocusedContainerColor = colors.surfaceColor,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.primary.copy(alpha = 0.35f),
                        focusedTextColor = colors.textColor,
                        unfocusedTextColor = colors.textColor,
                        cursorColor = colors.brightGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_input")
                )
            }
        }
    }
}
