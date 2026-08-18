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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoutiqueOrder
import com.example.data.model.OrderStatus
import com.example.ui.theme.AmberPending
import com.example.ui.theme.CrimsonUrgent
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun OrderCard(
    order: BoutiqueOrder,
    onViewDetails: () -> Unit,
    onAdvanceStatus: (OrderStatus) -> Unit,
    onCallCustomer: (String) -> Unit = {},
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val now = System.currentTimeMillis()
    val diffMillis = order.dateDueForDelivery - now
    val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

    val (timelineBadgeText, timelineBadgeColor) = when {
        order.orderStatus == OrderStatus.DELIVERED -> "Delivered" to EmeraldPaid
        diffMillis < 0 -> "Overdue by ${-diffDays}d" to CrimsonUrgent
        diffDays == 0L -> "Due Today" to CrimsonUrgent
        diffDays == 1L -> "Due Tomorrow" to AmberPending
        diffDays <= 7L -> "Due in ${diffDays}d" to colors.primary
        else -> "Due in ${diffDays}d" to colors.textMutedColor
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.2.dp, colors.borderGold, RoundedCornerShape(16.dp))
            .clickable { onViewDetails() }
            .testTag("order_card_${order.id}"),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Order Number, Suit Count, and Due Date Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.surfaceColor)
                            .border(0.8.dp, colors.primary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = order.orderNumber,
                            color = colors.brightGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.accentColor.copy(alpha = 0.4f))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${order.numberOfSuits} Suit${if (order.numberOfSuits > 1) "s" else ""} • ${order.standardSize}",
                            color = colors.textColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Delivery Timeline Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(timelineBadgeColor.copy(alpha = 0.2f))
                        .border(1.dp, timelineBadgeColor.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 9.dp, vertical = 3.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = timelineBadgeColor,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timelineBadgeText,
                            color = timelineBadgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Garment Title & Customer Info
            Text(
                text = order.suitType,
                color = colors.textColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = currentFont
            )

            if (order.fabricDetails.isNotBlank()) {
                Text(
                    text = order.fabricDetails,
                    color = colors.brightGold,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Customer",
                        tint = colors.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = order.customerName,
                        color = colors.textColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (order.customerPhone.isNotBlank()) {
                    IconButton(
                        onClick = { onCallCustomer(order.customerPhone) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call Customer",
                            tint = colors.brightGold,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tailoring Status Stepper Bar
            TailoringStatusStepper(
                currentStatus = order.orderStatus,
                onSelectStatus = onAdvanceStatus
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Financial Accounting Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.darkBackground)
                    .border(0.8.dp, colors.borderGold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOTAL AMOUNT",
                        color = colors.textMutedColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%.2f", order.totalAmount)}",
                        color = colors.textColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "RECEIVED",
                        color = colors.textMutedColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%.2f", order.receivedAmount)}",
                        color = EmeraldPaid,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "PENDING BALANCE",
                        color = colors.textMutedColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%.2f", order.pendingAmount)}",
                        color = if (order.pendingAmount > 0) AmberPending else EmeraldPaid,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dates Footer & Quick Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Taken: ${dateFormat.format(Date(order.dateTaken))}",
                        color = colors.textMutedColor,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "Due: ${dateFormat.format(Date(order.dateDueForDelivery))}",
                        color = colors.brightGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View Invoice / Specs",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TailoringStatusStepper(
    currentStatus: OrderStatus,
    onSelectStatus: (OrderStatus) -> Unit
) {
    val colors = LocalBoutiqueColors.current

    val stages = listOf(
        OrderStatus.ORDER_TAKEN to "Booked",
        OrderStatus.CUTTING to "Cutting",
        OrderStatus.STITCHING to "Stitch",
        OrderStatus.READY_FOR_FITTING to "Fitting",
        OrderStatus.READY_FOR_DELIVERY to "Ready",
        OrderStatus.DELIVERED to "Delivered"
    )

    val currentStep = currentStatus.step

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        stages.forEachIndexed { index, (status, label) ->
            val isPassed = status.step <= currentStep && currentStatus != OrderStatus.CANCELLED
            val isCurrent = status == currentStatus

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelectStatus(status) }
                    .padding(horizontal = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> colors.primary
                                isPassed -> colors.primary.copy(alpha = 0.7f)
                                else -> colors.surfaceColor
                            }
                        )
                        .border(
                            1.dp,
                            if (isPassed) colors.brightGold else colors.borderGold.copy(alpha = 0.3f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed && !isCurrent) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = colors.textColor,
                            modifier = Modifier.size(11.dp)
                        )
                    } else {
                        Text(
                            text = "${status.step}",
                            color = if (isCurrent) colors.darkBackground else colors.textColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = label,
                    color = if (isCurrent) colors.brightGold else if (isPassed) colors.textColor else colors.textMutedColor,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (index < stages.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(
                            if (stages[index + 1].first.step <= currentStep) colors.primary else colors.surfaceColor
                        )
                )
            }
        }
    }
}
