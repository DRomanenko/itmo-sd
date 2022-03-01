package com.github.dromanenko.posts

import com.github.dromanenko.data.Post
import com.github.dromanenko.reader.JsonExtractorImpl
import java.io.FileInputStream
import java.net.URL
import java.util.*

class VkApi {
    companion object {
        const val HOST = "api.vk.com"
        const val API_METHOD = "newsfeed.search"

        const val VERSION = "v"
        const val ACCESS_TOKEN = "access_token"

        const val QUERY = "q"
        const val START_TIME = "start_time"
        const val END_TIME = "end_time"
    }

    private val properties = Properties()

    private val version: String
        get() = properties.getProperty("vkapi.version")

    private val accessToken: String
        get() = properties.getProperty("vkapi.access.token")

    init {
        properties.load(FileInputStream("src/main/resources/application.properties"))
    }

    fun getPosts(query: String, startTime: Long?, endTime: Long?): List<Post> {
        val requestParameters = mapOf(
            VERSION to version,
            ACCESS_TOKEN to accessToken,
            QUERY to query,
            START_TIME to startTime,
            END_TIME to endTime
        ).entries.joinToString(separator = "&") { (if (it.value != null) "${it.key}=${it.value}" else "") }
        val url = URL("https://$HOST/method/$API_METHOD?${requestParameters}")
        return JsonExtractorImpl().extract(url)
    }
}