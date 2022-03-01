package com.github.dromanenko

import com.github.dromanenko.data.Post
import com.github.dromanenko.posts.VkApi
import com.github.dromanenko.posts.VkApiStats
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VkApiStatsTest {
    private lateinit var vkApiStats: VkApiStats

    @MockK
    lateinit var vkApi: VkApi

    @BeforeTest
    fun init() {
        MockKAnnotations.init(this)
        vkApiStats = VkApiStats(vkApi)
    }

    @Test
    fun `incorrect - number of hours`() {
        assertFailsWith<IllegalArgumentException> {
            listOf(-100, 0, 25).forEach { vkApiStats.getPostsStats("vk", it) }
        }
    }

    @Test
    fun `correct - no posts`() {
        every { vkApi.getPosts(any(), any(), any()) } returns listOf()
        assertEquals(vkApiStats.getPostsStats("test", 3), listOf(0, 0, 0))
    }

    @Test
    fun `correct - several posts`() {
        val currentTime = System.currentTimeMillis() / 1000
        val expectedList = listOf(
            Post(1, -2, currentTime - 5 * 60 * 60, "text"),
            Post(2, -1, currentTime - 2 * 60 * 60, "text"),
            Post(3, 0, currentTime - 3 * 60 * 60, "text"),
            Post(4, 1, currentTime - 3 * 60 * 60, "text"),
            Post(1_000_000, 1_000, currentTime - 2 * 60 * 60, "text"),
        )
        every { vkApi.getPosts(any(), any(), any()) } returns expectedList
        assertEquals(vkApiStats.getPostsStats("vk", 5), listOf(1, 0, 2, 2, 0))
    }
}