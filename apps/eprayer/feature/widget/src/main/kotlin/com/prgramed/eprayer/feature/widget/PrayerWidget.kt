package com.prgramed.eprayer.feature.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
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
import com.prgramed.eprayer.data.widget.PrayerWidgetWorker
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PrayerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = loadWidgetData(context)

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(20.dp)
                    .background(BgDark)
                    .padding(16.dp),
            ) {
                // Top row: next prayer name + time
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = data.nextName,
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        ),
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = data.nextTime,
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(Color.White),
                        ),
                    )
                }

                Spacer(modifier = GlanceModifier.height(16.dp))

                // Bottom row: all 5 prayers
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    for (i in data.prayers.indices) {
                        val prayer = data.prayers[i]
                        val isNext = prayer.name == data.nextName

                        Box(
                            modifier = GlanceModifier.defaultWeight(),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = if (isNext) {
                                    GlanceModifier
                                        .cornerRadius(12.dp)
                                        .background(Highlight)
                                        .padding(horizontal = 6.dp, vertical = 8.dp)
                                } else {
                                    GlanceModifier.padding(horizontal = 6.dp, vertical = 8.dp)
                                },
                            ) {
                                Text(
                                    text = prayer.name,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = if (isNext) FontWeight.Bold
                                            else FontWeight.Normal,
                                        color = ColorProvider(
                                            if (isNext) Color.White else TextMuted,
                                        ),
                                        textAlign = TextAlign.Center,
                                    ),
                                )
                                Spacer(modifier = GlanceModifier.height(4.dp))
                                Text(
                                    text = prayer.time,
                                    style = TextStyle(
                                        fontSize = 14.sp,
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

    private fun loadWidgetData(context: Context): WidgetData {
        val prefs = context.getSharedPreferences(
            PrayerWidgetWorker.PREFS_NAME, Context.MODE_PRIVATE,
        )

        val nextName = prefs.getString(PrayerWidgetWorker.KEY_NEXT_NAME, null)
        val nextTimeMillis = prefs.getLong(PrayerWidgetWorker.KEY_NEXT_TIME, 0L)

        if (nextName == null || nextTimeMillis == 0L) {
            return WidgetData(
                nextName = "ePrayer",
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

        fun fmtMillis(millis: Long): String =
            java.time.Instant.ofEpochMilli(millis).atZone(zone).format(formatter)

        val nextFormatted = fmtMillis(nextTimeMillis)

        val prayers = listOf(
            PrayerInfo("Fajr", fmtMillis(prefs.getLong(PrayerWidgetWorker.KEY_FAJR, 0L))),
            PrayerInfo("Dhuhr", fmtMillis(prefs.getLong(PrayerWidgetWorker.KEY_DHUHR, 0L))),
            PrayerInfo("Asr", fmtMillis(prefs.getLong(PrayerWidgetWorker.KEY_ASR, 0L))),
            PrayerInfo("Maghrib", fmtMillis(prefs.getLong(PrayerWidgetWorker.KEY_MAGHRIB, 0L))),
            PrayerInfo("Isha", fmtMillis(prefs.getLong(PrayerWidgetWorker.KEY_ISHA, 0L))),
        )

        return WidgetData(nextName = nextName, nextTime = nextFormatted, prayers = prayers)
    }

    private data class WidgetData(
        val nextName: String,
        val nextTime: String,
        val prayers: List<PrayerInfo>,
    )

    private data class PrayerInfo(val name: String, val time: String)

    companion object {
        private val BgDark = Color(0xFF1A2744)
        private val Highlight = Color(0x33FFFFFF)
        private val TextMuted = Color(0xAABFC9D9)
        private val Peach = Color(0xFFE8B98A)
    }
}
