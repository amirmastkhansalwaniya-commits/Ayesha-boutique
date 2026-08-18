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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPending
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont
import com.example.ui.viewmodel.FinancialStats
import java.util.Locale

@Composable
fun FinancialSummaryCard(
    stats: FinancialStats,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    FinancialSummaryCard(
        totalRevenue = stats.totalRevenue,
        totalCollected = stats.totalCollected,
        totalPending = stats.totalPending,
        paidCount = stats.fullyPaidCount,
        partialCount = stats.partialPaidCount,
        unpaidCount = stats.unpaidCount,
        currencySymbol = currencySymbol,
        modifier = modifier
    )
}

@Composable
fun FinancialSummaryCard(
    totalRevenue: Double,
    totalCollected: Double,
    totalPending: Double,
    paidCount: Int,
    partialCount: Int,
    unpaidCount: Int,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    val collectionRate = if (totalRevenue > 0) (totalCollected / totalRevenue).toFloat().coerceIn(0f, 1f) else 0f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.2.dp, colors.borderGold, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(colors.cardBackground, colors.surfaceColor)
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceColor)
                            .border(1.dp, colors.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Accounting",
                            tint = colors.brightGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "FINANCIAL ACCOUNTING",
                            color = colors.brightGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontFamily = currentFont
                        )
                        Text(
                            text = "Revenue, Collections & Balances",
                            color = colors.textMutedColor,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${(collectionRate * 100).toInt()}% Collected",
                        color = colors.brightGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main 3 financial KPIs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Billed
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.darkBackground)
                        .border(0.8.dp, colors.borderGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "TOTAL BILLED",
                            color = colors.textMutedColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currencySymbol${String.format(Locale.US, "%.0f", totalRevenue)}",
                            color = colors.textColor,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Total Collected Advance
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.darkBackground)
                        .border(0.8.dp, EmeraldPaid.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "COLLECTED",
                            color = colors.textMutedColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currencySymbol${String.format(Locale.US, "%.0f", totalCollected)}",
                            color = EmeraldPaid,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Balance Pending
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.darkBackground)
                        .border(0.8.dp, AmberPending.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "PENDING DUES",
                            color = colors.textMutedColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$currencySymbol${String.format(Locale.US, "%.0f", totalPending)}",
                            color = AmberPending,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Collection Progress Bar
            LinearProgressIndicator(
                progress = { collectionRate },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = colors.primary,
                trackColor = colors.darkBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Order status breakdown pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EmeraldPaid)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$paidCount Paid",
                        color = colors.textColor,
                        fontSize = 11.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AmberPending)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$partialCount Partial",
                        color = colors.textColor,
                        fontSize = 11.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE57373))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$unpaidCount Unpaid",
                        color = colors.textColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
