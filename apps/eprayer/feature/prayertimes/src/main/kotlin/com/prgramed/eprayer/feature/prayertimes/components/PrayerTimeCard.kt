package com.prgramed.eprayer.feature.prayertimes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prgramed.eprayer.domain.model.Prayer
import com.prgramed.eprayer.domain.model.PrayerTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val NavyCard = Color(0xFF162135)
private val NavyBorder = Color(0xFF1E2F47)
private val Peach = Color(0xFFE8B98A)
private val TextMuted = Color(0xFF8899AA)

@Composable
fun PrayerTimeCard(
    prayerTime: PrayerTime,
    notificationEnabled: Boolean,
    onNotificationToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = prayerTime.prayer.name.lowercase().replaceFirstChar { it.uppercase() }
    val formattedTime = formatTime(prayerTime)
    val isNext = prayerTime.isNext
    val showBell = prayerTime.prayer != Prayer.SUNRISE

    val borderColor = if (isNext) Peach.copy(alpha = 0.6f) else NavyBorder

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        color = NavyCard,
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = displayName,
                fontSize = 16.sp,
                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                color = if (isNext) Color.White else TextMuted,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = formattedTime,
                    fontSize = 16.sp,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                    color = if (isNext) Peach else Peach.copy(alpha = 0.7f),
                )
                if (showBell) {
                    IconButton(
                        onClick = onNotificationToggle,
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = if (notificationEnabled) {
                                Icons.Default.Notifications
                            } else {
                                Icons.Default.NotificationsOff
                            },
                            contentDescription = "Toggle notification",
                            tint = if (notificationEnabled) TextMuted else TextMuted.copy(alpha = 0.3f),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

private fun formatTime(prayerTime: PrayerTime): String {
    val instant = java.time.Instant.ofEpochMilli(prayerTime.time.toEpochMilliseconds())
    val zonedTime = instant.atZone(ZoneId.systemDefault())
    return DateTimeFormatter.ofPattern("HH:mm").format(zonedTime)
}
