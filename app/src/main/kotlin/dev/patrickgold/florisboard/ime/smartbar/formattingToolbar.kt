
package dev.patrickgold.florisboard.ime.smartbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.min

/**
 * First version of the Vocal-style text formatting toolbar for FlorisBoard.
 * This is a standalone UI component; it is not integrated into Smartbar yet.
 */
@Composable
fun FormattingToolbar(
    modifier: Modifier = Modifier,
    onBoldClick: () -> Unit = {},
    onItalicClick: () -> Unit = {},
    onUnderlineClick: () -> Unit = {},
    onFontClick: () -> Unit = {},
    onSizeClick: () -> Unit = {},
    onColorSelected: (Color) -> Unit = {},
) {
    var selectedColor by remember { mutableStateOf(Color.Black) }
    var showColors by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 6.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormatButton("B", bold = true, onClick = onBoldClick)
            FormatButton("I", italic = true, onClick = onItalicClick)
            FormatButton("U", underline = true, onClick = onUnderlineClick)
            FormatButton("Font", onClick = onFontClick)
            FormatButton("Size", onClick = onSizeClick)

            FormatButton(
                label = "Color",
                onClick = { showColors = !showColors },
                background = selectedColor.copy(alpha = 0.18f),
            )
        }

        if (showColors) {
            ColorWheel(
                selectedColor = selectedColor,
                onColorSelected = {
                    selectedColor = it
                    onColorSelected(it)
                },
            )
        }
    }
}

@Composable
private fun FormatButton(
    label: String,
    bold: Boolean = false,
    italic: Boolean = false,
    underline: Boolean = false,
    background: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = if (underline) {
                TextDecoration.Underline
            } else {
                TextDecoration.None
            },
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ColorWheel(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
) {
    val colors = listOf(
        Color.Red,
        Color(0xFFFF7F00),
        Color.Yellow,
        Color.Green,
        Color.Cyan,
        Color.Blue,
        Color.Magenta,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(
            modifier = Modifier
                .size(150.dp)
                .clickable {
                    onColorSelected(
                        colors.random()
                    )
                },
        ) {
            val radius = min(size.width, size.height) / 2f - 5.dp.toPx()
            val center = Offset(size.width / 2f, size.height / 2f)

            colors.forEachIndexed { index, color ->
                drawArc(
                    color = color,
                    startAngle = index * (360f / colors.size),
                    sweepAngle = 360f / colors.size,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(
                        radius * 2,
                        radius * 2,
                    ),
                )
            }

            drawCircle(
                color = Color.White,
                radius = radius * 0.30f,
                center = center,
            )

            drawCircle(
                color = selectedColor,
                radius = radius * 0.22f,
                center = center,
            )

            drawCircle(
                color = Color.Gray,
                radius = radius,
                center = center,
                style = Stroke(width = 1.dp.toPx()),
            )
        }

        Text(
            text = "Tap the wheel to choose a colour",
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
