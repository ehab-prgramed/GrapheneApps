package com.prgramed.eprayer.feature.qibla.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grapheneapps.core.designsystem.theme.Gold
import com.grapheneapps.core.designsystem.theme.LightGreen
import com.grapheneapps.core.designsystem.theme.MediumGreen

@Composable
fun CompassView(
    deviceHeading: Float,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.size(300.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val outerRadius = size.minDimension / 2 - 12.dp.toPx()
        val innerRadius = outerRadius - 36.dp.toPx()

        // Outer ring
        drawCircle(
            color = MediumGreen.copy(alpha = 0.3f),
            radius = outerRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3.dp.toPx()),
        )

        // Inner ring
        drawCircle(
            color = MediumGreen.copy(alpha = 0.15f),
            radius = innerRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.5.dp.toPx()),
        )

        // Center dot
        drawCircle(
            color = LightGreen.copy(alpha = 0.4f),
            radius = 4.dp.toPx(),
            center = Offset(centerX, centerY),
        )

        rotate(-deviceHeading, Offset(centerX, centerY)) {
            // Degree ticks
            for (i in 0 until 360 step 5) {
                val isMajor = i % 30 == 0
                val isMedium = i % 10 == 0
                val tickLength = when {
                    isMajor -> 14.dp.toPx()
                    isMedium -> 8.dp.toPx()
                    else -> 4.dp.toPx()
                }
                val tickColor = when {
                    i == 0 -> Gold
                    isMajor -> LightGreen
                    else -> LightGreen.copy(alpha = 0.3f)
                }
                val angle = Math.toRadians(i.toDouble())
                val startR = outerRadius - tickLength
                drawLine(
                    color = tickColor,
                    start = Offset(
                        centerX + (startR * kotlin.math.sin(angle)).toFloat(),
                        centerY - (startR * kotlin.math.cos(angle)).toFloat(),
                    ),
                    end = Offset(
                        centerX + (outerRadius * kotlin.math.sin(angle)).toFloat(),
                        centerY - (outerRadius * kotlin.math.cos(angle)).toFloat(),
                    ),
                    strokeWidth = if (isMajor) 2.dp.toPx() else 1.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }

            // Cardinal directions
            val directions = listOf(
                "N" to 0f, "E" to 90f, "S" to 180f, "W" to 270f,
            )
            for ((label, angle) in directions) {
                val radians = Math.toRadians(angle.toDouble())
                val textR = outerRadius - 28.dp.toPx()
                val color = if (label == "N") Gold else Color.White.copy(alpha = 0.8f)
                val style = TextStyle(
                    fontSize = if (label == "N") 18.sp else 14.sp,
                    fontWeight = if (label == "N") FontWeight.Bold else FontWeight.Medium,
                    color = color,
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

            // Intercardinal labels
            val intercardinals = listOf(
                "NE" to 45f, "SE" to 135f, "SW" to 225f, "NW" to 315f,
            )
            for ((label, angle) in intercardinals) {
                val radians = Math.toRadians(angle.toDouble())
                val textR = outerRadius - 28.dp.toPx()
                val style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.4f),
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
