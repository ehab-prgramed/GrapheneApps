package com.prgramed.eprayer.domain.scheduler

import com.prgramed.eprayer.domain.model.PrayerDay

interface PrayerScheduler {
    fun scheduleAlarms(prayerDay: PrayerDay, enabledPrayers: Set<String> = emptySet())
    fun cancelAllAlarms()
}
