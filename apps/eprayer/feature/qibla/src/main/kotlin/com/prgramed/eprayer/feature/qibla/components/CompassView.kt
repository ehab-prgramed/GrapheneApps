package com.prgramed.eprayer.feature.qibla.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CompassWhite = Color(0xFFF5F0EB)
private val CompassBorder = Color(0xFFE0D8CF)
private val CardinalGray = Color(0xFFB0A89E)

@Composable
fun CompassView(
    deviceHeading: Float,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.size(300.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2 - 8.dp.toPx()

        // Beige border ring
        drawCircle(
            color = CompassBorder,
            radius = radius + 4.dp.toPx(),
            center = Offset(centerX, centerY),
            style = Stroke(width = 8.dp.toPx()),
        )

        // White filled compass face
        drawCircle(
            color = CompassWhite,
            radius = radius,
            center = Offset(centerX, centerY),
        )

        // Cardinal directions rotate with compass
        rotate(-deviceHeading, Offset(centerX, centerY)) {
            val directions = listOf("N" to 0f, "E" to 90f, "S" to 180f, "W" to 270f)
            for ((label, angle) in directions) {
                val radians = Math.toRadians(angle.toDouble())
                val textR = radius - 30.dp.toPx()
                val style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = CardinalGray,
                )
                val measured = textMeasurer.measure(label, style)
                drawText(
                    textLayoutResult = measured,
                    topLeft = Offset(
                        centerX + (textR * kotlin.math.sin(radians)).toFloat()
                            - measured.size.width / 2,
                        centerY - (textR * kotlin.math.cos(radians)).toFloat()
                            - measured.size.height / 2,
                    ),
                )
            }
        }
    }
}
