package com.github.dromanenko

import com.github.dromanenko.posts.VkApi
import com.github.dromanenko.posts.VkApiStats
import java.util.*


fun main(args: Array<String>) {
    if (args.isEmpty() || args.any(Objects::isNull)) {
        System.err.println("The first argument of the program was expected to be a search query")
    } else {
        println("--- Posts from the last hour (max 30) ---")
        val hashtag = args[0]
        val vkAPI = VkApi()
        val endTime = System.currentTimeMillis() / 1000
        val startTime = endTime - 1 * 60 * 60
        val posts = vkAPI.getPosts("%23${hashtag}", startTime, endTime)
        posts.forEachIndexed { index, post ->
            println("№${index + 1} | post_id=${post.id} :- ${post.text.replace('\n', ' ')}")
        }

        println()

        println("--- Hourly statistics ---")
        val postStats = VkApiStats(vkAPI)
        for (hour in 1..24) {
            println("$hour (hours) :-")
            postStats.getPostsStats(hashtag, hour).forEachIndexed { index, stat ->
                if (index > 0)
                    println("\t№${index} :- $stat")
            }
        }
    }
}