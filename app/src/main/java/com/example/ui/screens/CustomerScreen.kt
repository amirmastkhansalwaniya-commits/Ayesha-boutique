package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import com.example.data.model.CustomAppSettings
import com.example.util.PdfReportGenerator
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Customer
import com.example.ui.dialogs.WhatsAppShareDialog
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MaroonCard
import com.example.ui.theme.MaroonDarkest
import com.example.ui.theme.MaroonSurface

private val WhatsAppGreen = Color(0xFF25D366)

@Composable
fun CustomerScreen(
    customers: List<Customer>,
    onAddCustomer: () -> Unit,
    onEditCustomer: (Customer) -> Unit,
    onDeleteCustomer: (Customer) -> Unit,
    onCallCustomer: (String) -> Unit,
    onGetCustomerShareText: ((Customer) -> String)? = null
) {
    var customerToShare by remember { mutableStateOf<Customer?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaroonDarkest)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Header summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CLIENT DIRECTORY (${customers.size})",
                    color = GoldLight,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "With Saved Measurements & WhatsApp",
                    color = ChampagneMuted,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (customers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GoldMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No clients found",
                            color = ChampagneSilk,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap the '+' button to register a new boutique client",
                            color = ChampagneMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(customers, key = { it.id }) { customer ->
                        CustomerCard(
                            customer = customer,
                            onEdit = { onEditCustomer(customer) },
                            onDelete = { onDeleteCustomer(customer) },
                            onCall = { onCallCustomer(customer.phone) },
                            onShareWhatsApp = { customerToShare = customer }
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onAddCustomer,
            containerColor = GoldPrimary,
            contentColor = MaroonDarkest,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp)
                .testTag("fab_new_customer")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Client", modifier = Modifier.size(24.dp))
        }
    }

    // WhatsApp Share Dialog for Client
    customerToShare?.let { customer ->
        val unit = customer.defaultMeasurementUnit ?: "in"
        val shareText = onGetCustomerShareText?.invoke(customer) ?: """
            ⚜️ *AYSHA BOUTIQUE • CLIENT DETAILS* ⚜️
            ━━━━━━━━━━━━━━━━━━━
            👤 *Name:* ${customer.name}
            📞 *Contact:* ${customer.phone}
            ${if (customer.address.isNotBlank()) "📍 *Address:* ${customer.address}\n" else ""}
            📏 *SAVED BODY MEASUREMENTS ($unit)*
            • Chest: ${customer.defaultChest ?: "—"}
            • Waist: ${customer.defaultWaist ?: "—"}
            • Hips: ${customer.defaultHips ?: "—"}
            • Shoulder: ${customer.defaultShoulder ?: "—"}
            • Sleeve: ${customer.defaultSleeve ?: "—"}
            • Trouser Length: ${customer.defaultTrouserLength ?: "—"}
            
            ${if (customer.notes.isNotBlank()) "📝 *Notes:* ${customer.notes}\n" else ""}━━━━━━━━━━━━━━━━━━━
            _This app created by Amir Khan_
        """.trimIndent()

        val context = LocalContext.current
        val dummySettings = CustomAppSettings()
        val pdfFile = remember(customer) {
            PdfReportGenerator.generateCustomerPdf(context, customer, dummySettings)
        }

        WhatsAppShareDialog(
            title = "Share Client Details",
            subtitle = "Send measurements to client or another number",
            clientName = customer.name,
            clientPhone = customer.phone,
            shareText = shareText,
            pdfFile = pdfFile,
            onDismiss = { customerToShare = null }
        )
    }
}

@Composable
fun CustomerCard(
    customer: Customer,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCall: () -> Unit,
    onShareWhatsApp: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldBorder.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable { onEdit() }
            .testTag("customer_card_${customer.id}"),
        colors = CardDefaults.cardColors(containerColor = MaroonCard)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaroonSurface)
                            .border(1.2.dp, GoldPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = customer.name.firstOrNull()?.toString() ?: "C",
                            color = GoldBright,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = customer.name,
                            color = ChampagneSilk,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = customer.phone,
                                color = GoldLight,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val context = LocalContext.current

                    // Direct PDF View / Open
                    IconButton(
                        onClick = {
                            val dummySettings = CustomAppSettings()
                            val pdfFile = PdfReportGenerator.generateCustomerPdf(context, customer, dummySettings)
                            if (pdfFile != null) {
                                PdfReportGenerator.openPdfDirectly(context, pdfFile)
                            }
                        },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaroonSurface)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = "Client PDF", tint = GoldLight, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // WhatsApp Share Icon
                    IconButton(
                        onClick = onShareWhatsApp,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(WhatsAppGreen)
                            .testTag("customer_whatsapp_button_${customer.id}")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "WhatsApp Share", tint = Color.White, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    if (customer.phone.isNotBlank()) {
                        IconButton(
                            onClick = onCall,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(GoldPrimary)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = MaroonDarkest, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaroonSurface)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = GoldLight, modifier = Modifier.size(16.dp))
                    }
                }
            }

            if (customer.address.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = ChampagneMuted, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = customer.address,
                        color = ChampagneText,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // Saved Measurements Quick Chips
            if (customer.defaultChest != null || customer.defaultWaist != null || customer.defaultTrouserLength != null) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Straighten, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Saved Sizes:", color = ChampagneMuted, fontSize = 10.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        customer.defaultChest?.let { Text("C: $it\"", color = GoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        customer.defaultWaist?.let { Text("W: $it\"", color = GoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        customer.defaultHips?.let { Text("H: $it\"", color = GoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        customer.defaultTrouserLength?.let { Text("L: $it\"", color = GoldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }

            if (customer.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Note: ${customer.notes}",
                    color = ChampagneMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

