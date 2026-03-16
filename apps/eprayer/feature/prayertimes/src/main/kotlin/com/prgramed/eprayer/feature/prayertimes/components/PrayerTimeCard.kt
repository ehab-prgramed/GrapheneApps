package com.prgramed.eprayer.feature.prayertimes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grapheneapps.core.designsystem.theme.Gold
import com.grapheneapps.core.designsystem.theme.MediumGreen
import com.prgramed.eprayer.domain.model.Prayer
import com.prgramed.eprayer.domain.model.PrayerTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PrayerTimeCard(
    prayerTime: PrayerTime,
    modifier: Modifier = Modifier,
) {
    val displayName = prayerTime.prayer.name.lowercase().replaceFirstChar { it.uppercase() }
    val formattedTime = formatTime(prayerTime)
    val icon = prayerIcon(prayerTime.prayer)

    val bgColor = if (prayerTime.isNext) {
        MediumGreen.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    val accentColor = if (prayerTime.isNext) Gold else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = displayName,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (prayerTime.isNext) FontWeight.Bold else FontWeight.Normal,
            )
        }
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (prayerTime.isNext) FontWeight.Bold else FontWeight.Normal,
            color = if (prayerTime.isNext) Gold else MaterialTheme.colorScheme.onSurface,
        )
    }
}

private fun prayerIcon(prayer: Prayer): ImageVector = when (prayer) {
    Prayer.FAJR -> Icons.Default.DarkMode
    Prayer.SUNRISE -> Icons.Default.WbSunny
    Prayer.DHUHR -> Icons.Default.LightMode
    Prayer.ASR -> Icons.Default.WbTwilight
    Prayer.MAGHRIB -> Icons.Default.WbTwilight
    Prayer.ISHA -> Icons.Default.Nightlight
}

private fun formatTime(prayerTime: PrayerTime): String {
    val instant = java.time.Instant.ofEpochMilli(prayerTime.time.toEpochMilliseconds())
    val zonedTime = instant.atZone(ZoneId.systemDefault())
    return DateTimeFormatter.ofPattern("hh:mm a").format(zonedTime)
}
