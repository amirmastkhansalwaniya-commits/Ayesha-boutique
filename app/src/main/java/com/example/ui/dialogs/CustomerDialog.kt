package com.example.ui.dialogs

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Customer
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MaroonCard
import com.example.ui.theme.MaroonDark
import com.example.ui.theme.MaroonDarkest
import com.example.ui.theme.MaroonSurface
import com.example.ui.theme.MaroonSurfaceVariant

@Composable
fun CustomerDialog(
    customer: Customer? = null,
    onDismiss: () -> Unit,
    onSave: (
        id: Long,
        name: String,
        phone: String,
        address: String,
        email: String,
        notes: String,
        chest: Double?,
        waist: Double?,
        hips: Double?,
        shoulder: Double?,
        sleeve: Double?,
        trouserLength: Double?,
        unit: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(customer?.name ?: "") }
    var phone by remember { mutableStateOf(customer?.phone ?: "") }
    var address by remember { mutableStateOf(customer?.address ?: "") }
    var email by remember { mutableStateOf(customer?.email ?: "") }
    var notes by remember { mutableStateOf(customer?.notes ?: "") }

    var unit by remember { mutableStateOf(customer?.defaultMeasurementUnit ?: "in") }
    var chestText by remember { mutableStateOf(customer?.defaultChest?.toString() ?: "") }
    var waistText by remember { mutableStateOf(customer?.defaultWaist?.toString() ?: "") }
    var hipsText by remember { mutableStateOf(customer?.defaultHips?.toString() ?: "") }
    var shoulderText by remember { mutableStateOf(customer?.defaultShoulder?.toString() ?: "") }
    var sleeveText by remember { mutableStateOf(customer?.defaultSleeve?.toString() ?: "") }
    var trouserLengthText by remember { mutableStateOf(customer?.defaultTrouserLength?.toString() ?: "") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, GoldBorder, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = MaroonCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaroonDarkest)
            ) {
                // Header
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = GoldBright,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (customer != null) "EDIT CLIENT PROFILE" else "NEW CLIENT REGISTRATION",
                                color = GoldLight,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Customer Management & Baseline Measurements",
                                color = ChampagneMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaroonSurface)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = GoldBright, modifier = Modifier.size(18.dp))
                    }
                }

                // Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "CONTACT & ADDRESS INFORMATION",
                        color = GoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Customer Full Name *", color = ChampagneMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("customer_name_input"),
                        colors = luxuryTextFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Number *", color = ChampagneMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("customer_phone_input"),
                        colors = luxuryTextFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Physical Address / Villa / Area *", color = ChampagneMuted) },
                        modifier = Modifier.fillMaxWidth().testTag("customer_address_input"),
                        colors = luxuryTextFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email (Optional)", color = ChampagneMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        colors = luxuryTextFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DEFAULT BODY MEASUREMENTS",
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        // Unit Selector
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaroonSurface)
                                .border(1.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (unit == "in") GoldPrimary else Color.Transparent)
                                    .clickable { unit = "in" }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "Inches (in)",
                                    color = if (unit == "in") MaroonDarkest else ChampagneText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (unit == "cm") GoldPrimary else Color.Transparent)
                                    .clickable { unit = "cm" }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    "Centimeters (cm)",
                                    color = if (unit == "cm") MaroonDarkest else ChampagneText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = chestText,
                            onValueChange = { chestText = it },
                            label = { Text("Chest ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = waistText,
                            onValueChange = { waistText = it },
                            label = { Text("Waist ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = hipsText,
                            onValueChange = { hipsText = it },
                            label = { Text("Hips ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = shoulderText,
                            onValueChange = { shoulderText = it },
                            label = { Text("Shoulder ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = sleeveText,
                            onValueChange = { sleeveText = it },
                            label = { Text("Sleeve ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = trouserLengthText,
                            onValueChange = { trouserLengthText = it },
                            label = { Text("Trouser ($unit)", color = ChampagneMuted) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            colors = luxuryTextFieldColors(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Client Preferences & Style Notes", color = ChampagneMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = luxuryTextFieldColors(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaroonDark)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneMuted)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(
                                    customer?.id ?: 0L,
                                    name,
                                    phone,
                                    address,
                                    email,
                                    notes,
                                    chestText.toDoubleOrNull(),
                                    waistText.toDoubleOrNull(),
                                    hipsText.toDoubleOrNull(),
                                    shoulderText.toDoubleOrNull(),
                                    sleeveText.toDoubleOrNull(),
                                    trouserLengthText.toDoubleOrNull(),
                                    unit
                                )
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = MaroonDarkest),
                        modifier = Modifier.testTag("save_customer_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (customer != null) "Update Client" else "Save Client",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
