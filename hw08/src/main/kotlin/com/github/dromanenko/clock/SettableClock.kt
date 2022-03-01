package com.github.dromanenko.clock

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class SettableClock : Clock() {
    private var clock : Clock = fixed(Instant.now(), systemUTC().zone)

    override fun instant(): Instant
        = clock.instant()

    override fun withZone(zone: ZoneId?): Clock
        = clock.withZone(getZone())

    override fun getZone(): ZoneId
        = clock.zone

    fun timeShift(duration : Duration) {
        clock = offset(clock, duration)
    }
}