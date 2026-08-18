package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.LocalBoutiqueColors
import java.io.File

@Composable
fun BoutiqueLogoView(
    customLogoPath: String?,
    logoSymbol: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    fontSize: Dp = 20.dp,
    borderColor: Color? = null,
    borderWidth: Dp = 1.5.dp
) {
    val colors = LocalBoutiqueColors.current
    val effectiveBorderColor = borderColor ?: colors.primary
    val context = LocalContext.current

    val hasCustomImage = !customLogoPath.isNullOrBlank() && File(customLogoPath).exists()

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(colors.accentColor, colors.darkBackground)
                )
            )
            .border(borderWidth, effectiveBorderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (hasCustomImage) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(File(customLogoPath!!))
                    .crossfade(true)
                    .build(),
                contentDescription = "Boutique Custom Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = logoSymbol.ifBlank { "✂️" },
                fontSize = (fontSize.value).sp
            )
        }
    }
}
