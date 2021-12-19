package com.github.dromanenko.reader

import com.github.dromanenko.data.Post
import java.net.URL

interface JsonExtractor {
    fun extract(url: URL) : List<Post>
}