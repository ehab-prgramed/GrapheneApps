package com.prgramed.eprayer.feature.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.prgramed.eprayer.data.widget.PrayerWidgetWorker
import java.util.concurrent.TimeUnit

class PrayerWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = PrayerWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        triggerImmediateRefresh(context)
        schedulePeriodicWorker(context)
    }

    private fun triggerImmediateRefresh(context: Context) {
        val request = OneTimeWorkRequestBuilder<PrayerWidgetWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }

    private fun schedulePeriodicWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<PrayerWidgetWorker>(
            30, TimeUnit.MINUTES,
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PrayerWidgetWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }
}
