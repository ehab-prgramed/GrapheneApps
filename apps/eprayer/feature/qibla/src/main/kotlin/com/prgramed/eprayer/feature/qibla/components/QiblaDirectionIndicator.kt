package com.prgramed.eprayer.feature.qibla.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.grapheneapps.core.designsystem.theme.Gold

@Composable
fun QiblaDirectionIndicator(
    relativeAngle: Double,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(300.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        rotate(relativeAngle.toFloat(), Offset(centerX, centerY)) {
            // Thin line from center to Kaaba
            drawLine(
                color = Gold.copy(alpha = 0.4f),
                start = Offset(centerX, centerY + 10.dp.toPx()),
                end = Offset(centerX, centerY - 90.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
            )

            // Small arrow tip
            val tipY = centerY - 90.dp.toPx()
            val arrowPath = Path().apply {
                moveTo(centerX, tipY - 6.dp.toPx())
                lineTo(centerX - 5.dp.toPx(), tipY + 2.dp.toPx())
                lineTo(centerX + 5.dp.toPx(), tipY + 2.dp.toPx())
                close()
            }
            drawPath(arrowPath, color = Gold)

            // Kaaba body
            val kaabaSize = 28.dp.toPx()
            val kaabaY = centerY - 118.dp.toPx()
            drawRoundRect(
                color = Color(0xFF3A3A3A),
                topLeft = Offset(centerX - kaabaSize / 2, kaabaY - kaabaSize / 2),
                size = Size(kaabaSize, kaabaSize),
                cornerRadius = CornerRadius(3.dp.toPx()),
            )

            // Gold band (Kiswa)
            val bandHeight = 5.dp.toPx()
            drawRect(
                color = Gold,
                topLeft = Offset(
                    centerX - kaabaSize / 2,
                    kaabaY - bandHeight / 2,
                ),
                size = Size(kaabaSize, bandHeight),
            )

            // Kaaba door
            val doorW = 6.dp.toPx()
            val doorH = 9.dp.toPx()
            drawRoundRect(
                color = Gold.copy(alpha = 0.7f),
                topLeft = Offset(
                    centerX - doorW / 2,
                    kaabaY + 2.dp.toPx(),
                ),
                size = Size(doorW, doorH),
                cornerRadius = CornerRadius(1.5.dp.toPx()),
            )
        }
    }
}
