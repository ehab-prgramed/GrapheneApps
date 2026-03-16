package com.prgramed.eprayer.feature.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PrayerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = readFromApp(context)
        val launchIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?: Intent()

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(16.dp)
                    .background(BgDark)
                    .clickable(actionStartActivity(launchIntent))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = data.nextName,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        ),
                    )
                    Spacer(modifier = GlanceModifier.width(16.dp))
                    Text(
                        text = data.nextTime,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        ),
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    for (prayer in data.prayers) {
                        val isNext = prayer.name == data.nextName
                        Box(
                            modifier = GlanceModifier.defaultWeight(),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = if (isNext) {
                                    GlanceModifier
                                        .cornerRadius(8.dp)
                                        .background(Highlight)
                                        .padding(horizontal = 2.dp, vertical = 4.dp)
                                } else {
                                    GlanceModifier.padding(horizontal = 2.dp, vertical = 4.dp)
                                },
                            ) {
                                Text(
                                    text = prayer.name,
                                    style = TextStyle(
                                        fontSize = 11.sp,
                                        fontWeight = if (isNext) FontWeight.Bold
                                        else FontWeight.Normal,
                                        color = ColorProvider(
                                            if (isNext) Color.White else TextMuted,
                                        ),
                                        textAlign = TextAlign.Center,
                                    ),
                                )
                                Text(
                                    text = prayer.time,
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                        fontWeight = if (isNext) FontWeight.Bold
                                        else FontWeight.Normal,
                                        color = ColorProvider(
                                            if (isNext) Color.White else Peach,
                                        ),
                                        textAlign = TextAlign.Center,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun readFromApp(context: Context): WidgetData {
        val sp = context.getSharedPreferences(WIDGET_PREFS, Context.MODE_PRIVATE)

        val fajrMillis = sp.getLong("fajr_millis", 0L)
        if (fajrMillis == 0L) {
            return WidgetData(
                nextName = "Open app",
                nextTime = "--:--",
                prayers = listOf(
                    PrayerInfo("Fajr", "--:--"),
                    PrayerInfo("Dhuhr", "--:--"),
                    PrayerInfo("Asr", "--:--"),
                    PrayerInfo("Maghrib", "--:--"),
                    PrayerInfo("Isha", "--:--"),
                ),
            )
        }

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val zone = ZoneId.systemDefault()

        fun fmt(millis: Long): String =
            java.time.Instant.ofEpochMilli(millis).atZone(zone).format(formatter)

        val nextName = sp.getString("next_prayer_name", "Fajr") ?: "Fajr"
        val nextMillis = sp.getLong("next_prayer_time_millis", 0L)

        val nowMillis = System.currentTimeMillis()
        val all = listOf(
            "Fajr" to sp.getLong("fajr_millis", 0L),
            "Dhuhr" to sp.getLong("dhuhr_millis", 0L),
            "Asr" to sp.getLong("asr_millis", 0L),
            "Maghrib" to sp.getLong("maghrib_millis", 0L),
            "Isha" to sp.getLong("isha_millis", 0L),
        )

        // Recalculate next prayer from the stored times (may have advanced since app wrote)
        val actualNext = all.firstOrNull { it.second > nowMillis } ?: all.first()

        return WidgetData(
            nextName = actualNext.first,
            nextTime = fmt(actualNext.second),
            prayers = all.map { PrayerInfo(it.first, fmt(it.second)) },
        )
    }

    private data class WidgetData(
        val nextName: String,
        val nextTime: String,
        val prayers: List<PrayerInfo>,
    )

    private data class PrayerInfo(val name: String, val time: String)

    companion object {
        private const val WIDGET_PREFS = "eprayer_widget_data"
        private val BgDark = Color(0xFF1A2744)
        private val Highlight = Color(0x33FFFFFF)
        private val TextMuted = Color(0xAABFC9D9)
        private val Peach = Color(0xFFE8B98A)
    }
}
