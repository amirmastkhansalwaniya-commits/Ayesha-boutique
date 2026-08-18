package com.example.ui.dialogs

import android.app.DatePickerDialog
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BodyMeasurements
import com.example.data.model.BoutiqueOrder
import com.example.data.model.Customer
import com.example.data.model.OrderStatus
import com.example.ui.theme.AmberPending
import com.example.ui.theme.ChampagneMuted
import com.example.ui.theme.ChampagneSilk
import com.example.ui.theme.ChampagneText
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderDialog(
    existingOrder: BoutiqueOrder? = null,
    customers: List<Customer>,
    onDismiss: () -> Unit,
    onSaveCustomer: (name: String, phone: String, address: String, chest: Double?, waist: Double?, hips: Double?, shoulder: Double?, sleeve: Double?, trouserLength: Double?, onComplete: (Long) -> Unit) -> Unit,
    currencySymbol: String = "₹",
    onSaveOrder: (
        id: Long,
        orderNumber: String,
        customerId: Long,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        numberOfSuits: Int,
        standardSize: String,
        suitType: String,
        fabricDetails: String,
        measurements: BodyMeasurements,
        totalAmount: Double,
        receivedAmount: Double,
        dateTaken: Long,
        dateDueForDelivery: Long,
        orderStatus: OrderStatus,
        specialInstructions: String
    ) -> Unit
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Step Tabs: 0: Customer & Specs, 1: Measurements, 2: Financial & Schedule
    var selectedTab by remember { mutableIntStateOf(0) }

    // Customer info
    var selectedCustomerId by remember { mutableLongStateOf(existingOrder?.customerId ?: (customers.firstOrNull()?.id ?: 0L)) }
    var customerName by remember { mutableStateOf(existingOrder?.customerName ?: (customers.firstOrNull()?.name ?: "")) }
    var customerPhone by remember { mutableStateOf(existingOrder?.customerPhone ?: (customers.firstOrNull()?.phone ?: "")) }
    var customerAddress by remember { mutableStateOf(existingOrder?.customerAddress ?: (customers.firstOrNull()?.address ?: "")) }
    var customerDropdownExpanded by remember { mutableStateOf(false) }

    // Garment specifications
    val garmentTypes = listOf(
        "Bridal Lehanga & Kurti",
        "3-Piece Tuxedo / Suit",
        "Salwar Kameez (Party Wear)",
        "Embroidered Anarkali Suit",
        "Abaya / Luxury Kaftan",
        "Formal Sherwani",
        "Velvet Evening Gown",
        "Kurta Pajama",
        "Bespoke Couture"
    )
    var suitType by remember { mutableStateOf(existingOrder?.suitType ?: garmentTypes.first()) }
    var garmentDropdownExpanded by remember { mutableStateOf(false) }

    var numberOfSuitsText by remember { mutableStateOf((existingOrder?.numberOfSuits ?: 1).toString()) }

    val standardSizes = listOf("Custom", "XS", "S", "M", "L", "XL", "2XL", "3XL")
    var standardSize by remember { mutableStateOf(existingOrder?.standardSize ?: "Custom") }

    var fabricDetails by remember { mutableStateOf(existingOrder?.fabricDetails ?: "") }
    var specialInstructions by remember { mutableStateOf(existingOrder?.specialInstructions ?: "") }

    // Measurements
    var measurementUnit by remember { mutableStateOf(existingOrder?.customMeasurements?.unit ?: "in") }
    var chestText by remember { mutableStateOf(existingOrder?.customMeasurements?.chest?.toString() ?: "") }
    var waistText by remember { mutableStateOf(existingOrder?.customMeasurements?.waist?.toString() ?: "") }
    var hipsText by remember { mutableStateOf(existingOrder?.customMeasurements?.hips?.toString() ?: "") }
    var shoulderText by remember { mutableStateOf(existingOrder?.customMeasurements?.shoulder?.toString() ?: "") }
    var sleeveText by remember { mutableStateOf(existingOrder?.customMeasurements?.sleeve?.toString() ?: "") }
    var trouserLengthText by remember { mutableStateOf(existingOrder?.customMeasurements?.trouserLength?.toString() ?: "") }
    var neckText by remember { mutableStateOf(existingOrder?.customMeasurements?.neck?.toString() ?: "") }
    var inseamText by remember { mutableStateOf(existingOrder?.customMeasurements?.inseam?.toString() ?: "") }
    var armholeText by remember { mutableStateOf(existingOrder?.customMeasurements?.armhole?.toString() ?: "") }
    var measurementNotes by remember { mutableStateOf(existingOrder?.customMeasurements?.measurementNotes ?: "") }

    // Financial
    var totalAmountText by remember { mutableStateOf(existingOrder?.totalAmount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var receivedAmountText by remember { mutableStateOf(existingOrder?.receivedAmount?.let { if (it > 0) it.toString() else "" } ?: "") }

    val totalAmountVal by remember {
        derivedStateOf { totalAmountText.toDoubleOrNull() ?: 0.0 }
    }
    val receivedAmountVal by remember {
        derivedStateOf { receivedAmountText.toDoubleOrNull() ?: 0.0 }
    }
    val pendingAmountVal by remember {
        derivedStateOf { (totalAmountVal - receivedAmountVal).coerceAtLeast(0.0) }
    }

    // Timeline Schedule
    var dateTaken by remember { mutableLongStateOf(existingOrder?.dateTaken ?: System.currentTimeMillis()) }
    var dateDueForDelivery by remember {
        mutableLongStateOf(
            existingOrder?.dateDueForDelivery ?: (System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000))
        )
    }
    var orderStatus by remember { mutableStateOf(existingOrder?.orderStatus ?: OrderStatus.ORDER_TAKEN) }

    fun showDatePicker(currentMillis: Long, onDateSelected: (Long) -> Unit) {
        val cal = Calendar.getInstance().apply { timeInMillis = currentMillis }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 18)
                    set(Calendar.MINUTE, 0)
                }
                onDateSelected(selectedCal.timeInMillis)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
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
                    Column {
                        Text(
                            text = if (existingOrder != null) "EDIT ORDER: ${existingOrder.orderNumber}" else "NEW BOUTIQUE ORDER",
                            color = GoldLight,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Customer Specs, Body Measurements & Schedule",
                            color = ChampagneMuted,
                            fontSize = 11.sp
                        )
                    }

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

                // Tabs: 1. Customer & Specs, 2. Measurements, 3. Financial & Dates
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaroonDark,
                    contentColor = GoldPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldPrimary,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "1. Specs",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) GoldBright else ChampagneMuted
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "2. Measurements",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) GoldBright else ChampagneMuted
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Text(
                                "3. Financial & Dates",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) GoldBright else ChampagneMuted
                            )
                        }
                    )
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedTab) {
                        0 -> {
                            // MODULE 1 & 2: CUSTOMER & GARMENT SPECS
                            Text(
                                text = "CUSTOMER DETAILS",
                                color = GoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Customer dropdown or selector
                            if (customers.isNotEmpty()) {
                                ExposedDropdownMenuBox(
                                    expanded = customerDropdownExpanded,
                                    onExpandedChange = { customerDropdownExpanded = !customerDropdownExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = customerName,
                                        onValueChange = {
                                            customerName = it
                                        },
                                        label = { Text("Customer Name *", color = ChampagneMuted) },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = customerDropdownExpanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        colors = luxuryTextFieldColors(),
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    ExposedDropdownMenu(
                                        expanded = customerDropdownExpanded,
                                        onDismissRequest = { customerDropdownExpanded = false },
                                        modifier = Modifier.background(MaroonSurface)
                                    ) {
                                        customers.forEach { c ->
                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text(c.name, color = ChampagneSilk, fontWeight = FontWeight.SemiBold)
                                                        Text("${c.phone} • ${c.address}", color = ChampagneMuted, fontSize = 11.sp)
                                                    }
                                                },
                                                onClick = {
                                                    selectedCustomerId = c.id
                                                    customerName = c.name
                                                    customerPhone = c.phone
                                                    customerAddress = c.address

                                                    // Auto-fill customer saved baseline measurements if present!
                                                    if (c.defaultChest != null && chestText.isBlank()) chestText = c.defaultChest.toString()
                                                    if (c.defaultWaist != null && waistText.isBlank()) waistText = c.defaultWaist.toString()
                                                    if (c.defaultHips != null && hipsText.isBlank()) hipsText = c.defaultHips.toString()
                                                    if (c.defaultShoulder != null && shoulderText.isBlank()) shoulderText = c.defaultShoulder.toString()
                                                    if (c.defaultSleeve != null && sleeveText.isBlank()) sleeveText = c.defaultSleeve.toString()
                                                    if (c.defaultTrouserLength != null && trouserLengthText.isBlank()) trouserLengthText = c.defaultTrouserLength.toString()
                                                    measurementUnit = c.defaultMeasurementUnit

                                                    customerDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                OutlinedTextField(
                                    value = customerName,
                                    onValueChange = { customerName = it },
                                    label = { Text("Customer Name *", color = ChampagneMuted) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = customerPhone,
                                    onValueChange = { customerPhone = it },
                                    label = { Text("Contact Number *", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                OutlinedTextField(
                                    value = customerAddress,
                                    onValueChange = { customerAddress = it },
                                    label = { Text("Address / City", color = ChampagneMuted) },
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "ORDER & GARMENT SPECIFICATIONS",
                                color = GoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Garment Style dropdown
                            ExposedDropdownMenuBox(
                                expanded = garmentDropdownExpanded,
                                onExpandedChange = { garmentDropdownExpanded = !garmentDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = suitType,
                                    onValueChange = { suitType = it },
                                    label = { Text("Suit / Garment Type *", color = ChampagneMuted) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = garmentDropdownExpanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = garmentDropdownExpanded,
                                    onDismissRequest = { garmentDropdownExpanded = false },
                                    modifier = Modifier.background(MaroonSurface)
                                ) {
                                    garmentTypes.forEach { type ->
                                        DropdownMenuItem(
                                            text = { Text(type, color = ChampagneSilk) },
                                            onClick = {
                                                suitType = type
                                                garmentDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = numberOfSuitsText,
                                    onValueChange = { numberOfSuitsText = it },
                                    label = { Text("No. of Suits *", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Standard Size", color = ChampagneMuted, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaroonSurface)
                                            .border(1.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                            .padding(4.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        listOf("Custom", "S", "M", "L", "XL").forEach { size ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (standardSize == size) GoldPrimary else Color.Transparent)
                                                    .clickable { standardSize = size }
                                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = size,
                                                    color = if (standardSize == size) MaroonDarkest else ChampagneText,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (standardSize == size) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = fabricDetails,
                                onValueChange = { fabricDetails = it },
                                label = { Text("Fabric & Embroidery Details", color = ChampagneMuted) },
                                placeholder = { Text("e.g., Pure Raw Silk with Gold Dabka Zari work", color = ChampagneMuted.copy(alpha = 0.5f)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = luxuryTextFieldColors(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        1 -> {
                            // MODULE 2: CUSTOM BODY MEASUREMENTS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "CUSTOM BODY MEASUREMENTS",
                                        color = GoldLight,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Precision tailoring dimensions",
                                        color = ChampagneMuted,
                                        fontSize = 11.sp
                                    )
                                }

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
                                            .background(if (measurementUnit == "in") GoldPrimary else Color.Transparent)
                                            .clickable { measurementUnit = "in" }
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "Inches (in)",
                                            color = if (measurementUnit == "in") MaroonDarkest else ChampagneText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (measurementUnit == "cm") GoldPrimary else Color.Transparent)
                                            .clickable { measurementUnit = "cm" }
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "Centimeters (cm)",
                                            color = if (measurementUnit == "cm") MaroonDarkest else ChampagneText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Measurement Inputs: Chest, Waist, Hips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = chestText,
                                    onValueChange = { chestText = it },
                                    label = { Text("Chest ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = waistText,
                                    onValueChange = { waistText = it },
                                    label = { Text("Waist ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = hipsText,
                                    onValueChange = { hipsText = it },
                                    label = { Text("Hips ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Measurement Inputs: Shoulder, Sleeve, Trouser Length
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = shoulderText,
                                    onValueChange = { shoulderText = it },
                                    label = { Text("Shoulder ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = sleeveText,
                                    onValueChange = { sleeveText = it },
                                    label = { Text("Sleeve ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = trouserLengthText,
                                    onValueChange = { trouserLengthText = it },
                                    label = { Text("Trouser L. ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Additional parameters: Neck, Inseam, Armhole
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = neckText,
                                    onValueChange = { neckText = it },
                                    label = { Text("Neck ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = inseamText,
                                    onValueChange = { inseamText = it },
                                    label = { Text("Inseam ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = armholeText,
                                    onValueChange = { armholeText = it },
                                    label = { Text("Armhole ($measurementUnit)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = measurementNotes,
                                onValueChange = { measurementNotes = it },
                                label = { Text("Measurement / Cut Customizations", color = ChampagneMuted) },
                                placeholder = { Text("e.g. Deep back neckline, loose fit at thigh, flare trouser", color = ChampagneMuted.copy(alpha = 0.5f)) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = luxuryTextFieldColors(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        2 -> {
                            // MODULE 3 & 4: FINANCIAL ACCOUNTING & TIMELINE SCHEDULING
                            Text(
                                text = "FINANCIAL ACCOUNTING",
                                color = GoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = totalAmountText,
                                    onValueChange = { totalAmountText = it },
                                    label = { Text("Total Amount ($currencySymbol) *", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                OutlinedTextField(
                                    value = receivedAmountText,
                                    onValueChange = { receivedAmountText = it },
                                    label = { Text("Advance Received ($currencySymbol)", color = ChampagneMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1f),
                                    colors = luxuryTextFieldColors(),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Auto-calculated Pending Amount Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaroonDarkest)
                                    .border(1.dp, if (pendingAmountVal > 0) AmberPending else EmeraldPaid, RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "AUTOMATIC BALANCE CALCULATION",
                                            color = ChampagneMuted,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = if (pendingAmountVal > 0) "Pending / Due Amount" else "Fully Paid in Advance",
                                            color = ChampagneSilk,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = "$currencySymbol${String.format(Locale.US, "%.2f", pendingAmountVal)}",
                                        color = if (pendingAmountVal > 0) AmberPending else EmeraldPaid,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "TIMELINE SCHEDULING",
                                color = GoldLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Date Taken and Delivery Due Date
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Date Taken
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaroonSurface)
                                        .border(1.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                        .clickable {
                                            showDatePicker(dateTaken) { dateTaken = it }
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text("Date Taken (Booking)", color = ChampagneMuted, fontSize = 10.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(dateFormat.format(Date(dateTaken)), color = ChampagneSilk, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }

                                // Date Due for Delivery
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaroonSurface)
                                        .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
                                        .clickable {
                                            showDatePicker(dateDueForDelivery) { dateDueForDelivery = it }
                                        }
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text("Date Due for Delivery *", color = GoldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = GoldBright, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(dateFormat.format(Date(dateDueForDelivery)), color = GoldBright, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = specialInstructions,
                                onValueChange = { specialInstructions = it },
                                label = { Text("Special Delivery / Fitting Instructions", color = ChampagneMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = luxuryTextFieldColors(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }

                // Footer Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaroonDark)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedTab > 0) {
                        OutlinedButton(
                            onClick = { selectedTab-- },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(GoldBorder, GoldDark)))
                        ) {
                            Text("Back")
                        }
                    } else {
                        OutlinedButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneMuted)
                        ) {
                            Text("Cancel")
                        }
                    }

                    if (selectedTab < 2) {
                        Button(
                            onClick = { selectedTab++ },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = MaroonDarkest)
                        ) {
                            Text("Next Step", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (customerName.isNotBlank()) {
                                    val measurements = BodyMeasurements(
                                        chest = chestText.toDoubleOrNull(),
                                        waist = waistText.toDoubleOrNull(),
                                        hips = hipsText.toDoubleOrNull(),
                                        shoulder = shoulderText.toDoubleOrNull(),
                                        sleeve = sleeveText.toDoubleOrNull(),
                                        trouserLength = trouserLengthText.toDoubleOrNull(),
                                        neck = neckText.toDoubleOrNull(),
                                        inseam = inseamText.toDoubleOrNull(),
                                        armhole = armholeText.toDoubleOrNull(),
                                        unit = measurementUnit,
                                        measurementNotes = measurementNotes
                                    )

                                    val count = numberOfSuitsText.toIntOrNull() ?: 1

                                    if (selectedCustomerId == 0L) {
                                        // Save customer first then order
                                        onSaveCustomer(
                                            customerName,
                                            customerPhone,
                                            customerAddress,
                                            measurements.chest,
                                            measurements.waist,
                                            measurements.hips,
                                            measurements.shoulder,
                                            measurements.sleeve,
                                            measurements.trouserLength
                                        ) { newCustId ->
                                            onSaveOrder(
                                                existingOrder?.id ?: 0L,
                                                existingOrder?.orderNumber ?: "",
                                                newCustId,
                                                customerName,
                                                customerPhone,
                                                customerAddress,
                                                count,
                                                standardSize,
                                                suitType,
                                                fabricDetails,
                                                measurements,
                                                totalAmountVal,
                                                receivedAmountVal,
                                                dateTaken,
                                                dateDueForDelivery,
                                                orderStatus,
                                                specialInstructions
                                            )
                                        }
                                    } else {
                                        onSaveOrder(
                                            existingOrder?.id ?: 0L,
                                            existingOrder?.orderNumber ?: "",
                                            selectedCustomerId,
                                            customerName,
                                            customerPhone,
                                            customerAddress,
                                            count,
                                            standardSize,
                                            suitType,
                                            fabricDetails,
                                            measurements,
                                            totalAmountVal,
                                            receivedAmountVal,
                                            dateTaken,
                                            dateDueForDelivery,
                                            orderStatus,
                                            specialInstructions
                                        )
                                    }
                                }
                            },
                            enabled = customerName.isNotBlank() && suitType.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldBright, contentColor = MaroonDarkest),
                            modifier = Modifier.testTag("save_order_button")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (existingOrder != null) "Update Order" else "Create Order",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun luxuryTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaroonSurface,
    unfocusedContainerColor = MaroonSurface,
    focusedBorderColor = GoldPrimary,
    unfocusedBorderColor = GoldBorder.copy(alpha = 0.35f),
    focusedTextColor = ChampagneSilk,
    unfocusedTextColor = ChampagneSilk,
    focusedLabelColor = GoldBright,
    unfocusedLabelColor = ChampagneMuted,
    cursorColor = GoldBright
)
