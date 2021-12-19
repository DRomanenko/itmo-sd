package com.github.dromanenko.reader

import com.github.dromanenko.data.Post
import com.github.dromanenko.data.VkApiResponse
import com.google.gson.Gson
import java.net.URL

class JsonExtractorImpl : JsonExtractor {
    override fun extract(url: URL): List<Post> {
        val json = url.readText()
        return Gson().fromJson(json, VkApiResponse::class.java).response?.items ?: listOf()
    }
}