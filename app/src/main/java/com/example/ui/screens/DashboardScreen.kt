package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoutiqueOrder
import com.example.data.model.OrderStatus
import com.example.ui.components.FinancialSummaryCard
import com.example.ui.components.OrderCard
import com.example.ui.theme.AmberPending
import com.example.ui.theme.CrimsonUrgent
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.LocalBoutiqueColors
import com.example.ui.theme.LocalBoutiqueFont
import com.example.ui.viewmodel.FinancialStats
import com.example.ui.viewmodel.TimelineStats
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    orders: List<BoutiqueOrder>,
    financialStats: FinancialStats,
    timelineStats: TimelineStats,
    onOpenNewOrder: () -> Unit,
    onOpenNewCustomer: () -> Unit,
    onViewOrder: (BoutiqueOrder) -> Unit,
    onAdvanceOrderStatus: (BoutiqueOrder, OrderStatus) -> Unit,
    onCallCustomer: (String) -> Unit,
    onNavigateToOrders: () -> Unit,
    currencySymbol: String = "₹",
    boutiqueName: String = "Aysha Boutique"
) {
    val colors = LocalBoutiqueColors.current
    val currentFont = LocalBoutiqueFont.current

    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    val todayFormatted = dateFormat.format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.darkBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Welcome & Quick Action Bar
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
                            Brush.verticalGradient(
                                colors = listOf(colors.accentColor.copy(alpha = 0.5f), colors.cardBackground)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Text(
                        text = todayFormatted.uppercase(),
                        color = colors.brightGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$boutiqueName Atelier",
                        color = colors.textColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )

                    Text(
                        text = "Daily Operations, Custom Tailoring & Order Ledger",
                        color = colors.textMutedColor,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rapid Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenNewOrder,
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.darkBackground),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_new_order_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Order", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = onOpenNewCustomer,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.brightGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_new_customer_button")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Client", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Production Status Summary Cards
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ATELIER PRODUCTION STATUS",
                    color = colors.brightGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardMetricBadge(
                        title = "Active Orders",
                        count = timelineStats.totalActive,
                        icon = Icons.Default.HourglassTop,
                        iconTint = AmberPending,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardMetricBadge(
                        title = "Due Today",
                        count = timelineStats.dueToday,
                        icon = Icons.Default.Alarm,
                        iconTint = colors.brightGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DashboardMetricBadge(
                        title = "Overdue",
                        count = timelineStats.overdue,
                        icon = Icons.Default.Warning,
                        iconTint = CrimsonUrgent,
                        modifier = Modifier.weight(1f)
                    )
                    DashboardMetricBadge(
                        title = "Ready for Delivery",
                        count = timelineStats.readyForDelivery,
                        icon = Icons.Default.CheckCircle,
                        iconTint = EmeraldPaid,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Financial Ledger Overview Card
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "FINANCIAL SUMMARY",
                    color = colors.brightGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                FinancialSummaryCard(
                    stats = financialStats,
                    currencySymbol = currencySymbol
                )
            }
        }

        // Urgent & Due Soon Orders List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE ORDERS (${orders.size})",
                    color = colors.brightGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onNavigateToOrders() }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "View All",
                        color = colors.brightGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "View All",
                        tint = colors.brightGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        if (orders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, colors.borderGold, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Checkroom,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No Orders in Atelier",
                            color = colors.textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap 'New Order' above to take the first bespoke tailoring order.",
                            color = colors.textMutedColor,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(orders.take(5)) { order ->
                OrderCard(
                    order = order,
                    onViewDetails = { onViewOrder(order) },
                    onAdvanceStatus = { onAdvanceOrderStatus(order, it) },
                    onCallCustomer = { onCallCustomer(order.customerPhone) },
                    currencySymbol = currencySymbol
                )
            }
        }
    }
}

@Composable
fun DashboardMetricBadge(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalBoutiqueColors.current

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, colors.borderGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = colors.textMutedColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = count.toString(),
                    color = colors.textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
