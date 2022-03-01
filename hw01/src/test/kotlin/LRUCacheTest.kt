import org.junit.platform.commons.util.LruCache
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals

class LRUCacheTest {
    @Test
    fun `throw AssertionError - incorrect initial value of limitSize`() {
        for (limitSize in -10..0) {
            assertFailsWith<AssertionError> {
                LRUCache<Int?, Int?>(limitSize)
            }
        }
    }

    @Test
    fun `throw AssertionError - key or value is null`() {
        val cache = LRUCache<Int?, Int?>(1)
        assertFailsWith<AssertionError> {
            cache.put(1, null)
        }
        assertFailsWith<AssertionError> {
            cache.put(null, 1)
        }
        assertFailsWith<AssertionError> {
            cache.put(null, null)
        }
        assertFailsWith<AssertionError> {
            cache.get(null)
        }
        assertFailsWith<AssertionError> {
            cache.exists(1, null)
        }
        assertFailsWith<AssertionError> {
            cache.exists(null, 1)
        }
        assertFailsWith<AssertionError> {
            cache.exists(null, null)
        }
    }

    @Test
    fun `correct - adding single item`() {
        val (key, value) = 1 to 1
        val myCache = LRUCache<Int?, Int?>(1)
        myCache.put(key, value)
        assertEquals(myCache.exists(key, value), true)
        assertEquals(myCache.get(key), value)
    }

    @Test
    fun `correct - adding many number of items (comparison with LruCache)`() {
        val expectedCache = LruCache<Int, Int>(25)
        val cache = LRUCache<Int, Int>(25)
        for (key in 0..1_000_000) {
            val value = key + 25
            expectedCache[key] = value
            cache.put(key, value)
            assertEquals(cache.size, expectedCache.size)
            assertEquals(cache.exists(key, value), expectedCache[key] == value)
            assertEquals(cache.get(key), expectedCache[key])
        }

        for (key in 0..1_000) {
            expectedCache[key] = key
            cache.put(key, key)
            assertEquals(cache.size, expectedCache.size)
            assertEquals(cache.exists(key, key), expectedCache[key] == key)
            assertEquals(cache.get(key), expectedCache[key])
        }
    }
}