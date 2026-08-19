package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BoutiqueOrder
import com.example.data.model.CustomAppSettings
import com.example.data.model.OrderStatus
import com.example.ui.components.MeasurementViewer
import com.example.ui.components.TailoringStatusStepper
import com.example.ui.dialogs.WhatsAppShareDialog
import com.example.util.PdfReportGenerator
import com.example.ui.theme.AmberPending
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
import com.example.ui.theme.CrimsonUrgent
import com.example.ui.theme.EmeraldPaid
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MaroonAccent
import com.example.ui.theme.MaroonCard
import com.example.ui.theme.MaroonDark
import com.example.ui.theme.MaroonDarkest
import com.example.ui.theme.MaroonSurface
import com.example.ui.theme.MaroonSurfaceVariant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderDetailDialog(
    order: BoutiqueOrder,
    onDismiss: () -> Unit,
    onEditOrder: () -> Unit,
    onDeleteOrder: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit,
    onRecordPayment: (Double) -> Unit,
    receiptText: String,
    currencySymbol: String = "₹"
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    var showWhatsAppShareDialog by remember { mutableStateOf(false) }

    fun shareReceipt() {
        showWhatsAppShareDialog = true
    }

    fun exportPdfInvoice() {
        val dummySettings = CustomAppSettings()
        val pdfFile = PdfReportGenerator.generateSingleOrderInvoicePdf(context, order, dummySettings)
        if (pdfFile != null) {
            PdfReportGenerator.openPdfDirectly(context, pdfFile)
        }
    }

    fun callCustomer() {
        if (order.customerPhone.isNotBlank()) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${order.customerPhone}")
            }
            context.startActivity(intent)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, GoldBorder, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = MaroonCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonDarkest)
            ) {
                // Luxury Invoice Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(MaroonSurfaceVariant, MaroonDark)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ORDER: ${order.orderNumber}",
                                color = GoldLight,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Serif
                            )
                        }
                        Text(
                            text = "Aysha Boutique • Bespoke Order Sheet",
                            color = ChampagneMuted,
                            fontSize = 11.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { shareReceipt() },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaroonSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Receipt",
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaroonSurface)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = GoldBright,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Customer & Contact Summary Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaroonSurface)
                            .border(1.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CLIENT DETAILS",
                                    color = ChampagneMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = order.customerName,
                                    color = ChampagneSilk,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = order.customerPhone,
                                    color = GoldLight,
                                    fontSize = 13.sp
                                )
                                if (order.customerAddress.isNotBlank()) {
                                    Text(
                                        text = order.customerAddress,
                                        color = ChampagneText.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (order.customerPhone.isNotBlank()) {
                                IconButton(
                                    onClick = { callCustomer() },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = MaroonDarkest,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Suit Specification
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaroonSurface)
                            .border(1.dp, GoldBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "GARMENT SPECIFICATIONS",
                                    color = ChampagneMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${order.numberOfSuits} Suit(s) • Size: ${order.standardSize}",
                                    color = GoldBright,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = order.suitType,
                                color = ChampagneSilk,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (order.fabricDetails.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Fabric: ${order.fabricDetails}",
                                    color = ChampagneText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Measurement Sheet Component
                    MeasurementViewer(measurements = order.customMeasurements)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tailoring Stage Stepper
                    Text(
                        text = "WORK ORDER PROGRESS",
                        color = GoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TailoringStatusStepper(
                        currentStatus = order.orderStatus,
                        onSelectStatus = onUpdateStatus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Financial Accounting & Balance Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaroonDark)
                            .border(1.2.dp, GoldBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "FINANCIAL SUMMARY",
                                    color = GoldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                if (order.pendingAmount > 0) {
                                    Button(
                                        onClick = { showRecordPaymentDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = MaroonDarkest),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = ButtonDefaults.ContentPadding
                                    ) {
                                        Icon(Icons.Default.MonetizationOn, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("+ Record Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Total Amount", color = ChampagneMuted, fontSize = 11.sp)
                                    Text(
                                        "$currencySymbol${String.format(Locale.US, "%.2f", order.totalAmount)}",
                                        color = ChampagneSilk,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column {
                                    Text("Received Advance", color = ChampagneMuted, fontSize = 11.sp)
                                    Text(
                                        "$currencySymbol${String.format(Locale.US, "%.2f", order.receivedAmount)}",
                                        color = EmeraldPaid,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Balance Pending", color = ChampagneMuted, fontSize = 11.sp)
                                    Text(
                                        "$currencySymbol${String.format(Locale.US, "%.2f", order.pendingAmount)}",
                                        color = if (order.pendingAmount > 0) AmberPending else EmeraldPaid,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Timeline Dates
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Date Taken", color = ChampagneMuted, fontSize = 11.sp)
                            Text(
                                dateFormat.format(Date(order.dateTaken)),
                                color = ChampagneSilk,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Delivery Due Date", color = ChampagneMuted, fontSize = 11.sp)
                            Text(
                                dateFormat.format(Date(order.dateDueForDelivery)),
                                color = GoldBright,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (order.specialInstructions.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Special Notes: ${order.specialInstructions}",
                            color = ChampagneMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                // Bottom Action Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaroonDark)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDeleteOrder,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonUrgent),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(CrimsonUrgent, CrimsonUrgent)))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }

                    Row {
                        OutlinedButton(
                            onClick = { exportPdfInvoice() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(GoldBorder, GoldDark)))
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF")
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedButton(
                            onClick = onEditOrder,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(GoldBorder, GoldDark)))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit")
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Button(
                            onClick = { shareReceipt() },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = MaroonDarkest)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showWhatsAppShareDialog) {
        val dummySettings = CustomAppSettings()
        val pdfFile = remember(order) {
            PdfReportGenerator.generateSingleOrderInvoicePdf(context, order, dummySettings)
        }
        WhatsAppShareDialog(
            title = "Share Order PDF",
            subtitle = "Order: ${order.orderNumber} • ${order.suitType}",
            clientName = order.customerName,
            clientPhone = order.customerPhone,
            shareText = receiptText,
            pdfFile = pdfFile,
            onDismiss = { showWhatsAppShareDialog = false }
        )
    }

    if (showRecordPaymentDialog) {
        RecordPaymentDialog(
            currentPending = order.pendingAmount,
            onDismiss = { showRecordPaymentDialog = false },
            onConfirmPayment = { amount ->
                onRecordPayment(amount)
                showRecordPaymentDialog = false
            },
            currencySymbol = currencySymbol
        )
    }
}
