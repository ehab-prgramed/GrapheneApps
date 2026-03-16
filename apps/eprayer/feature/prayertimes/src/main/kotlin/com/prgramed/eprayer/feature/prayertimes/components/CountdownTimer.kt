package com.prgramed.eprayer.feature.prayertimes.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.grapheneapps.core.designsystem.theme.Gold
import kotlin.time.Duration

@Composable
fun CountdownTimer(
    duration: Duration,
    modifier: Modifier = Modifier,
) {
    val totalSeconds = duration.inWholeSeconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val formatted = "%02d:%02d:%02d".format(hours, minutes, seconds)

    Text(
        text = formatted,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        color = Gold,
        modifier = modifier,
    )
}
