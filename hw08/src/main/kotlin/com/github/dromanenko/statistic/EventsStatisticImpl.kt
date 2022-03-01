package com.github.dromanenko.statistic

import java.time.Clock
import java.time.Duration
import java.time.Instant

class EventsStatisticImpl(private val clock: Clock) : EventsStatistic {
    companion object {
        private val LIFE_TIME = Duration.ofHours(1)
    }

    private val statistic = mutableMapOf<String, MutableList<Instant>>()

    override fun incEvent(name: String) {
        if (!statistic.containsKey(name)) statistic[name] = mutableListOf()
        statistic[name]!!.add(clock.instant())
    }

    override fun getEventStatisticByName(name: String): Double {
        val now = clock.instant()
        var count = 0
        val times = statistic[name] ?: return 0.0
        times.forEach { time -> if (now - LIFE_TIME <= time && time <= now) count++ }
        return count.div(60.0)
    }

    override fun getAllEventStatistic(): Map<String, Double> {
        val result = mutableMapOf<String, Double>()
        statistic.keys.forEach { event -> result[event] = getEventStatisticByName(event) }
        return result
    }

    override fun printStatistic() {
        val result = StringBuilder()
        result.appendLine("Statistics:")
        getAllEventStatistic().forEach { (key, value) ->
            result.appendLine("\t${key}: RPM = $value")
        }
        println(result)
    }
}