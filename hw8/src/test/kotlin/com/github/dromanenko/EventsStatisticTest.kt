package com.github.dromanenko

import com.github.dromanenko.clock.SettableClock
import com.github.dromanenko.statistic.EventsStatisticImpl
import java.time.Duration
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EventsStatisticTest {
    companion object {
        const val NAME = "test"
    }
    
    private lateinit var settableClock: SettableClock
    private lateinit var eventsStatistic: EventsStatisticImpl

    @BeforeTest
    fun init() {
        settableClock = SettableClock()
        eventsStatistic = EventsStatisticImpl(settableClock)
    }

    @Test
    fun `correct - no events`() {
        val expectedTime = 0.0
        assertEquals(expectedTime, eventsStatistic.getEventStatisticByName(NAME))
        assertEquals(mapOf(), eventsStatistic.getAllEventStatistic())
    }

    @Test
    fun `correct - one incEvent`() {
        val expectedTime = 1 / 60.0
        eventsStatistic.incEvent(NAME)
        assertEquals(expectedTime, eventsStatistic.getEventStatisticByName(NAME))
        assertEquals(mapOf(NAME to expectedTime), eventsStatistic.getAllEventStatistic())
    }

    @Test
    fun `correct - after one hour`() {
        val expectedTime = 1 / 60.0
        eventsStatistic.incEvent(NAME)
        settableClock.timeShift(Duration.ofMinutes(60))
        assertEquals(expectedTime, eventsStatistic.getEventStatisticByName(NAME))
        assertEquals(mapOf(NAME to expectedTime), eventsStatistic.getAllEventStatistic())

        settableClock.timeShift(Duration.ofMinutes(1))
        assertEquals(0.0, eventsStatistic.getEventStatisticByName(NAME))
        assertEquals(mapOf(NAME to 0.0), eventsStatistic.getAllEventStatistic())
    }

    @Test
    fun `correct - repeating event`() {
        (1..120).forEach { _ ->
            eventsStatistic.incEvent(NAME)
            settableClock.timeShift(Duration.ofMinutes(1))
        }
        assertEquals(1.0, eventsStatistic.getEventStatisticByName(NAME))
        assertEquals(mapOf(NAME to 1.0), eventsStatistic.getAllEventStatistic())
    }

    @Test
    fun `correct - too many events`() {
        val mod = 20
        val qty = 2400
        val expected = mutableMapOf<String, Double>()
        (0..qty).forEach {
            expected["${NAME}${it % mod}"] = 1.0 / mod
        }
        (0..qty).forEach {
            eventsStatistic.incEvent("${NAME}${it % mod}")
            settableClock.timeShift(Duration.ofMinutes(1))
        }
        (0..qty).forEach {
            assertEquals( 1.0 / mod, eventsStatistic.getEventStatisticByName("${NAME}${it % mod}"))
        }
        assertEquals(expected, eventsStatistic.getAllEventStatistic())
        eventsStatistic.printStatistic()
    }
}