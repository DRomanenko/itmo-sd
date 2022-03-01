interface Cache<K, V> {
    fun put(key: K, value: V) : Boolean

    fun get(key: K): V?

    fun exists(key: K, value: V): Boolean

    val size: Int
}