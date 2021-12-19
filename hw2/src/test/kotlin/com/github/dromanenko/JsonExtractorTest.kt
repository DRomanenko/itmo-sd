package com.github.dromanenko

import com.github.dromanenko.data.Post
import com.github.dromanenko.reader.JsonExtractorImpl
import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.semantics.Condition.startsWithUri
import com.xebialabs.restito.server.StubServer
import org.glassfish.grizzly.http.Method
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonExtractorTest {
    private val extractor = JsonExtractorImpl()

    private val url: URL
        get() = URL("http://localhost:${32453}/posts")

    private fun withStubServer(callback: StubServer.() -> Unit) {
        val stubServer = StubServer(32453)
        try {
            stubServer.run()
            stubServer.callback()
        } finally {
            stubServer.stop()
        }
    }

    @Test
    fun `correct - extract posts`() {
        val json = """
            {
                "response": {
                "items": [],
                "count": 0,
                "total_count": 0
                }
            }"""

        withStubServer() {
            whenHttp(this)
                .match(method(Method.GET), startsWithUri("/posts"))
                .then(stringContent(json))
            val result = extractor.extract(url)
            val expected = listOf<Post>()
            assertEquals(expected, result)
        }
    }
}