package com.example.ui.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalBoutiqueColors
import com.example.util.PdfReportGenerator
import java.io.File

private val WhatsAppGreen = Color(0xFF25D366)
private val WhatsAppDarkGreen = Color(0xFF128C7E)

@Composable
fun WhatsAppShareDialog(
    title: String,
    subtitle: String,
    clientName: String,
    clientPhone: String,
    shareText: String,
    pdfFile: File? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalBoutiqueColors.current

    var customPhoneNumber by remember { mutableStateOf("") }

    fun sendToWhatsApp(phone: String) {
        val cleanNumber = phone.replace(Regex("[^0-9]"), "")
        if (cleanNumber.isBlank()) {
            Toast.makeText(context, "कृपया मान्य व्हाट्सएप नंबर दर्ज करें", Toast.LENGTH_SHORT).show()
            return
        }

        if (pdfFile != null && pdfFile.exists()) {
            PdfReportGenerator.sharePdfOnWhatsApp(
                context = context,
                file = pdfFile,
                targetPhone = cleanNumber,
                caption = "📄 Official PDF Document - $clientName\n\n$shareText"
            )
            onDismiss()
            return
        }

        try {
            val finalPhone = if (cleanNumber.length == 10) "91$cleanNumber" else cleanNumber
            val encodedText = Uri.encode(shareText)
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$finalPhone&text=$encodedText")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            onDismiss()
        } catch (e: Exception) {
            Toast.makeText(context, "व्हाट्सएप खोलने में त्रुटि: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareViaChooser() {
        if (pdfFile != null && pdfFile.exists()) {
            PdfReportGenerator.openOrSharePdf(context, pdfFile, title)
            onDismiss()
            return
        }

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share via...")
        context.startActivity(shareIntent)
        onDismiss()
    }

    fun openPdfDirectly() {
        if (pdfFile != null && pdfFile.exists()) {
            PdfReportGenerator.openPdfDirectly(context, pdfFile)
            onDismiss()
        } else {
            Toast.makeText(context, "PDF फ़ाइल उपलब्ध नहीं है", Toast.LENGTH_SHORT).show()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, colors.brightGold, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = colors.cardBackground)
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
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(WhatsAppGreen.copy(alpha = 0.2f))
                                .border(1.2.dp, WhatsAppGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = WhatsAppGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = title,
                                color = colors.textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = subtitle,
                                color = colors.brightGold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceColor)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = colors.textColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // OPTION 1: Send directly to Client's Registered WhatsApp
                if (clientPhone.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        WhatsAppDarkGreen.copy(alpha = 0.35f),
                                        colors.surfaceColor
                                    )
                                )
                            )
                            .border(1.2.dp, WhatsAppGreen, RoundedCornerShape(14.dp))
                            .clickable { sendToWhatsApp(clientPhone) }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "1. ग्राहक के व्हाट्सएप नंबर पर भेजें",
                                    color = WhatsAppGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = clientName,
                                    color = colors.textColor,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = clientPhone,
                                    color = colors.brightGold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = { sendToWhatsApp(clientPhone) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = WhatsAppGreen,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("भेजें (Send)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = colors.borderGold.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // OPTION 2: Send to ANOTHER / Custom WhatsApp Number
                Text(
                    text = "2. किसी अन्य व्हाट्सएप नंबर पर भेजें (Master Tailor / Other Number)",
                    color = colors.brightGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customPhoneNumber,
                    onValueChange = { customPhoneNumber = it },
                    label = { Text("दूसरा मोबाइल नंबर दर्ज करें", color = colors.textMutedColor) },
                    placeholder = { Text("उदा. 9876543210 या +91...", color = colors.textMutedColor.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = colors.brightGold,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth().testTag("custom_whatsapp_phone_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surfaceColor,
                        unfocusedContainerColor = colors.surfaceColor,
                        focusedBorderColor = WhatsAppGreen,
                        unfocusedBorderColor = colors.borderGold.copy(alpha = 0.5f),
                        focusedTextColor = colors.textColor,
                        unfocusedTextColor = colors.textColor,
                        cursorColor = WhatsAppGreen
                    ),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { sendToWhatsApp(customPhoneNumber) },
                    enabled = customPhoneNumber.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppDarkGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("send_to_custom_whatsapp_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("इस नए नंबर पर व्हाट्सएप करें", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // OPTION 3: General System Share / Copy
                OutlinedButton(
                    onClick = { shareViaChooser() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = colors.brightGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("अन्य ऐप्स पर शेयर करें (Share via other apps)", fontSize = 12.sp)
                }
            }
        }
    }
}
