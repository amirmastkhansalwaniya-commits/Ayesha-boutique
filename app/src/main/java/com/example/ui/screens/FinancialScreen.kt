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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoutiqueOrder
import com.example.data.model.PaymentStatus
import com.example.ui.components.FinancialSummaryCard
import com.example.ui.dialogs.RecordPaymentDialog
import com.example.ui.theme.AmberPending
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MaroonCard
import com.example.ui.theme.MaroonDarkest
import com.example.ui.theme.MaroonSurface
import com.example.ui.viewmodel.FinancialStats
import java.util.Locale

@Composable
fun FinancialScreen(
    orders: List<BoutiqueOrder>,
    financialStats: FinancialStats,
    onRecordPayment: (BoutiqueOrder, Double) -> Unit,
    onViewOrder: (BoutiqueOrder) -> Unit,
    currencySymbol: String = "₹"
) {
    var selectedOrderForPayment by remember { mutableStateOf<BoutiqueOrder?>(null) }
    val pendingOrders = orders.filter { it.pendingAmount > 0 }
    val paidOrders = orders.filter { it.paymentStatus == PaymentStatus.PAID }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaroonDarkest)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Accounting Summary
        item {
            FinancialSummaryCard(
                totalRevenue = financialStats.totalRevenue,
                totalCollected = financialStats.totalCollected,
                totalPending = financialStats.totalPending,
                paidCount = financialStats.fullyPaidCount,
                partialCount = financialStats.partialPaidCount,
                unpaidCount = financialStats.unpaidCount,
                currencySymbol = currencySymbol
            )
        }

        // Section: Outstanding Balances & Pending Dues Ledger
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = AmberPending,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OUTSTANDING DUES LEDGER (${pendingOrders.size})",
                        color = GoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                Text(
                    text = "Total Pending: $currencySymbol${String.format(Locale.US, "%.0f", financialStats.totalPending)}",
                    color = AmberPending,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (pendingOrders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaroonSurface)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPaid, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "All customer dues are fully cleared!",
                            color = ChampagneSilk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(pendingOrders, key = { it.id }) { order ->
                PendingLedgerCard(
                    order = order,
                    onRecordPayment = { selectedOrderForPayment = order },
                    onView = { onViewOrder(order) },
                    currencySymbol = currencySymbol
                )
            }
        }

        // Section: Paid Orders History
        if (paidOrders.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPaid, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FULLY SETTLED ORDERS (${paidOrders.size})",
                        color = GoldLight,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            items(paidOrders.take(5), key = { "paid_${it.id}" }) { order ->
                SettledLedgerCard(
                    order = order,
                    onView = { onViewOrder(order) },
                    currencySymbol = currencySymbol
                )
            }
        }
    }

    selectedOrderForPayment?.let { order ->
        RecordPaymentDialog(
            currentPending = order.pendingAmount,
            onDismiss = { selectedOrderForPayment = null },
            onConfirmPayment = { amount ->
                onRecordPayment(order, amount)
                selectedOrderForPayment = null
            },
            currencySymbol = currencySymbol
        )
    }
}

@Composable
fun PendingLedgerCard(
    order: BoutiqueOrder,
    onRecordPayment: () -> Unit,
    onView: () -> Unit,
    currencySymbol: String = "₹"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, AmberPending.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
            .clickable { onView() },
        colors = CardDefaults.cardColors(containerColor = MaroonCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.customerName,
                        color = ChampagneSilk,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "${order.orderNumber} • ${order.suitType}",
                        color = ChampagneMuted,
                        fontSize = 11.sp
                    )
                }

                // Pending Pill
                Column(horizontalAlignment = Alignment.End) {
                    Text("PENDING", color = AmberPending, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%.2f", order.pendingAmount)}",
                        color = AmberPending,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaroonDarkest)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: $currencySymbol${String.format(Locale.US, "%.2f", order.totalAmount)}  |  Adv: $currencySymbol${String.format(Locale.US, "%.2f", order.receivedAmount)}",
                    color = ChampagneText,
                    fontSize = 11.sp
                )

                Button(
                    onClick = onRecordPayment,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = MaroonDarkest),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ Collect", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SettledLedgerCard(
    order: BoutiqueOrder,
    onView: () -> Unit,
    currencySymbol: String = "₹"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(0.8.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onView() },
        colors = CardDefaults.cardColors(containerColor = MaroonCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = order.customerName,
                    color = ChampagneSilk,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${order.orderNumber} • ${order.suitType}",
                    color = ChampagneMuted,
                    fontSize = 11.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("FULLY PAID", color = EmeraldPaid, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "$currencySymbol${String.format(Locale.US, "%.2f", order.totalAmount)}",
                    color = EmeraldPaid,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
