package com.github.dromanenko

import com.github.dromanenko.posts.VkApi
import kotlin.test.Test
import kotlin.test.assertTrue

class VkApiTest {
    private fun createVkApiClient() = VkApi()

    @Test
    fun getPosts() {
        val vkApiClient = createVkApiClient()
        val endTime = System.currentTimeMillis() / 1000
        val startTime = endTime - 24 * 60 * 60
        val posts = vkApiClient.getPosts("vk", startTime, endTime)
        assertTrue(posts.isNotEmpty())
    }
}