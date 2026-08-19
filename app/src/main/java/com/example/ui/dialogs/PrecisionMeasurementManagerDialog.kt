package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PrecisionCategory
import com.example.data.model.PrecisionMeasurementCatalog
import com.example.data.model.PrecisionMeasurementOption
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
import com.example.ui.theme.MaroonSurfaceVariant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddPrecisionMeasurementDialog(
    currentUnit: String,
    onDismiss: () -> Unit,
    onAddOption: (name: String, initialValue: String) -> Unit,
    extraPresets: List<PrecisionMeasurementOption> = emptyList()
) {
    var selectedCategory by remember { mutableStateOf(PrecisionCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var optionName by remember { mutableStateOf("") }
    var optionHindiName by remember { mutableStateOf("") }
    var initialValue by remember { mutableStateOf("") }

    val allOptions = remember(extraPresets) {
        (PrecisionMeasurementCatalog.defaultOptions + extraPresets).distinctBy { it.name.lowercase().trim() }
    }

    val filteredOptions = remember(selectedCategory, searchQuery, allOptions) {
        allOptions.filter { opt ->
            val matchCat = selectedCategory == PrecisionCategory.ALL || opt.category == selectedCategory
            val matchSearch = searchQuery.isBlank() ||
                    opt.name.contains(searchQuery, ignoreCase = true) ||
                    opt.hindiName.contains(searchQuery, ignoreCase = true) ||
                    opt.description.contains(searchQuery, ignoreCase = true)
            matchCat && matchSearch
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .heightIn(max = 680.dp)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaroonDarkest),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaroonSurface)
                                .border(1.dp, GoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Straighten,
                                contentDescription = null,
                                tint = GoldBright,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ADD PRECISION MEASUREMENT",
                                color = GoldLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Text(
                                text = "Choose from 50+ tailoring specs or create custom",
                                color = ChampagneMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = ChampagneMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search specs (e.g., neck, flare, गला, मोरी, thigh)...", color = ChampagneMuted.copy(alpha = 0.6f), fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GoldBright, modifier = Modifier.size(18.dp)) },
                    trailingIcon = if (searchQuery.isNotBlank()) {
                        {
                            IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(22.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = ChampagneMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = luxuryPrecisionTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(PrecisionCategory.values()) { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldPrimary else MaroonSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldBright else GoldBorder.copy(alpha = 0.35f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${category.iconEmoji} ${category.displayName}",
                                color = if (isSelected) MaroonDarkest else ChampagneText,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Catalog Suggestions Grid / FlowRow
                Text(
                    text = "Quick Pick Precision Options (${filteredOptions.size}):",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filteredOptions.forEach { opt ->
                        val isSelected = optionName.equals(opt.name, ignoreCase = true) ||
                                (opt.hindiName.isNotBlank() && optionName.contains(opt.name, ignoreCase = true))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldPrimary.copy(alpha = 0.9f) else MaroonSurfaceVariant)
                                .border(
                                    0.8.dp,
                                    if (isSelected) GoldBright else GoldBorder.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    optionName = if (opt.hindiName.isNotBlank()) "${opt.name} (${opt.hindiName})" else opt.name
                                }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Column {
                                Text(
                                    text = opt.name,
                                    color = if (isSelected) MaroonDarkest else ChampagneText,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                if (opt.hindiName.isNotBlank()) {
                                    Text(
                                        text = opt.hindiName,
                                        color = if (isSelected) MaroonDarkest.copy(alpha = 0.8f) else GoldLight.copy(alpha = 0.8f),
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input Custom Option Fields
                Text(
                    text = "OPTION DETAILS",
                    color = GoldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = optionName,
                    onValueChange = { optionName = it },
                    label = { Text("Measurement Name / Label *", color = ChampagneMuted) },
                    placeholder = { Text("e.g. Front Neck Depth, Flare (घेरा), Inseam", color = ChampagneMuted.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = luxuryPrecisionTextFieldColors(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = initialValue,
                    onValueChange = { initialValue = it },
                    label = { Text("Measurement Value / Dimension ($currentUnit)", color = ChampagneMuted) },
                    placeholder = { Text("e.g. 7.5, 42, 14.25", color = ChampagneMuted.copy(alpha = 0.5f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = luxuryPrecisionTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Text(
                            currentUnit,
                            color = GoldLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneMuted),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (optionName.isNotBlank()) {
                                onAddOption(optionName.trim(), initialValue.trim())
                                onDismiss()
                            }
                        },
                        enabled = optionName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = MaroonDarkest
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Measurement", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EditPrecisionMeasurementDialog(
    currentName: String,
    currentValue: String,
    currentUnit: String,
    onDismiss: () -> Unit,
    onSave: (newName: String, newValue: String) -> Unit,
    onDelete: () -> Unit
) {
    var editedName by remember { mutableStateOf(currentName) }
    var editedValue by remember { mutableStateOf(currentValue) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaroonDarkest),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = GoldBright,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EDIT MEASUREMENT OPTION",
                            color = GoldLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = ChampagneMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Measurement Name / Label *", color = ChampagneMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = luxuryPrecisionTextFieldColors(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = editedValue,
                    onValueChange = { editedValue = it },
                    label = { Text("Dimension Value ($currentUnit)", color = ChampagneMuted) },
                    placeholder = { Text("e.g. 8.5", color = ChampagneMuted.copy(alpha = 0.5f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    colors = luxuryPrecisionTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        Text(
                            currentUnit,
                            color = GoldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            onDelete()
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFF3F111B),
                            contentColor = Color(0xFFF87171)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF87171).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", fontSize = 12.sp)
                    }

                    Row {
                        OutlinedButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ChampagneMuted),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (editedName.isNotBlank()) {
                                    onSave(editedName.trim(), editedValue.trim())
                                    onDismiss()
                                }
                            },
                            enabled = editedName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = MaroonDarkest
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save Changes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrecisionOptionRow(
    index: Int,
    name: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaroonSurface)
            .border(0.8.dp, GoldBorder.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onEditClick() }
        ) {
            Text(
                text = name,
                color = GoldBright,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Tap ✏️ to rename or customize",
                color = ChampagneMuted.copy(alpha = 0.7f),
                fontSize = 9.5.sp
            )
        }

        // Value Input Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("0.0", color = ChampagneMuted.copy(alpha = 0.4f), fontSize = 11.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.width(100.dp),
            colors = luxuryPrecisionTextFieldColors(),
            shape = RoundedCornerShape(6.dp),
            trailingIcon = {
                Text(
                    unit,
                    color = ChampagneMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Edit Button
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaroonCard)
                .border(0.5.dp, GoldBorder.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Option",
                tint = GoldLight,
                modifier = Modifier.size(15.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Delete Button
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF3F111B))
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Delete Option",
                tint = Color(0xFFF87171),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun luxuryPrecisionTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaroonSurface,
    unfocusedContainerColor = MaroonSurfaceVariant.copy(alpha = 0.4f),
    focusedBorderColor = GoldBright,
    unfocusedBorderColor = GoldBorder.copy(alpha = 0.4f),
    focusedTextColor = ChampagneSilk,
    unfocusedTextColor = ChampagneSilk,
    focusedLabelColor = GoldLight,
    unfocusedLabelColor = ChampagneMuted,
    cursorColor = GoldBright
)
