import java.util.*

class LRUCache<K, V>(private val limitSize : Int) : Cache<K, V> {
    private val list = LinkedList<K>()
    private val data = hashMapOf<K, V>()

    init {
        assert(limitSize > 0) { "The size limit can't be less than 1" }
    }

    override fun put(key: K, value: V) : Boolean {
        assertNotNull(key, "key")
        assertNotNull(value, "value")
        return if (data.containsKey(key)) {
            data[key] = value
            list.addFirst(key)
            list.remove(key)
            false
        } else {
            data[key] = value
            list.addFirst(key)
            if (data.size > limitSize) {
                data.remove(list.removeLast())
            }
            assert(data.size >= list.size) { "Once added, the size cannot be reduced" }
            assertSize()
            true
        }
    }

    override fun get(key: K): V? {
        assertNotNull(key, "key")
        val value = data[key] ?: return null
        list.remove(key)
        list.addFirst(key)
        return value
    }

    override fun exists(key: K, value: V): Boolean {
        assertNotNull(key, "key")
        assertNotNull(value, "value")
        return get(key) == value
    }

    override val size: Int
        get() {
            assertSize()
            return list.size
        }

    private fun assertNotNull(any : Any?, nameParameter: String) =
        assert(any != null) { "$nameParameter must not be null" }

    private fun assertSize() =
        assert(data.size in 0..limitSize) { "The number of elements must be in the range from 1 to limitSize" }
}