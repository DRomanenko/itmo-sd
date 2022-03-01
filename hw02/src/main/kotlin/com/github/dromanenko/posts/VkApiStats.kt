package com.github.dromanenko.posts

class VkApiStats(private val client: VkApi) {
    companion object {
        const val CHARACTER_HASHTAG = "%23"
        const val MILLISECONDS = 1000
        const val SECONDS = 60
        const val MINUTES = 60
    }

    fun getPostsStats(hashtag: String, hours: Int): List<Int> {
        if (hours !in 1..24)
            throw IllegalArgumentException("Number of hours must be from 1 to 24")

        val endTime = System.currentTimeMillis() / MILLISECONDS
        val startTime = endTime - SECONDS * MINUTES * hours

        val stats = MutableList(hours + 1) { 0 }

        client.getPosts("$CHARACTER_HASHTAG$hashtag", startTime, endTime)
            .asSequence()
            .map { hours - ((endTime - it.date) / MINUTES / SECONDS).toInt() }
            .filter { it in 0..hours }
            .forEach { stats[it]++ }

        return stats.subList(0, stats.size - 1)
    }
}