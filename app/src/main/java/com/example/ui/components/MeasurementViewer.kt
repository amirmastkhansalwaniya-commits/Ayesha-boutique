package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BodyMeasurements
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MaroonCard
import com.example.ui.theme.MaroonDarkest
import com.example.ui.theme.MaroonSurface

@Composable
fun MeasurementViewer(
    measurements: BodyMeasurements,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaroonDarkest)
            .border(1.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Straighten,
                    contentDescription = "Measurements",
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CUSTOM BODY MEASUREMENTS",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaroonSurface)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (measurements.unit == "cm") "Metric (cm)" else "Imperial (inches)",
                    color = ChampagneMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Grid of measurements (Chest, Waist, Hips, Shoulder, Sleeve, Trouser Length)
        val items = listOfNotNull(
            "Chest" to measurements.chest,
            "Waist" to measurements.waist,
            "Hips" to measurements.hips,
            "Shoulder" to measurements.shoulder,
            "Sleeve" to measurements.sleeve,
            "Trouser L." to measurements.trouserLength,
            measurements.neck?.let { "Neck" to it },
            measurements.inseam?.let { "Inseam" to it },
            measurements.armhole?.let { "Armhole" to it },
            measurements.thigh?.let { "Thigh" to it }
        )

        // Render in pairs / triplets
        val chunked = items.chunked(3)
        chunked.forEachIndexed { index, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { (label, value) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaroonCard)
                            .border(0.5.dp, GoldBorder.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Column {
                            Text(
                                text = label,
                                color = ChampagneMuted,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "${value ?: "—"} ${measurements.unit}",
                                color = if (value != null) GoldBright else ChampagneMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                // Fill empty slots in the row if any
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (measurements.measurementNotes.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Notes: ${measurements.measurementNotes}",
                color = ChampagneText.copy(alpha = 0.8f),
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}
