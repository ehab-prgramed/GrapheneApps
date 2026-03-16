package com.prgramed.eprayer.feature.prayertimes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.grapheneapps.core.designsystem.theme.MediumGreen
import com.prgramed.eprayer.domain.model.PrayerDay

@Composable
fun DayProgressIndicator(
    prayerDay: PrayerDay,
    modifier: Modifier = Modifier,
) {
    val times = prayerDay.times
    if (times.size < 2) return

    val fajrMillis = times.first().time.toEpochMilliseconds().toFloat()
    val ishaMillis = times.last().time.toEpochMilliseconds().toFloat()
    val nowMillis = System.currentTimeMillis().toFloat()

    val progress = ((nowMillis - fajrMillis) / (ishaMillis - fajrMillis)).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Fajr",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Isha",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MediumGreen,
            trackColor = MediumGreen.copy(alpha = 0.15f),
        )
    }
}
