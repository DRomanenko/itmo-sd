package com.github.dromanenko.database

import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClients
import com.github.dromanenko.util.Currency
import com.github.dromanenko.entity.BaseEntity
import com.github.dromanenko.entity.Item
import com.github.dromanenko.entity.User
import org.bson.Document
import rx.Observable

class MongoUtils {
    companion object {
        const val DATABASE_NAME = "hw10"
        const val COLLECTION_USER_NAME = "User"
        const val COLLECTION_ITEM_NAME = "Item"
    }

    private val database = MongoClients.create("mongodb://localhost:27017").getDatabase(DATABASE_NAME)

    private val users = database.getCollection(COLLECTION_USER_NAME)

    private val items: Observable<Item>
        get() = users.find().toObservable().map { document: Document -> Item(document) }

    private fun <T : BaseEntity?> insert(collectionName: String, entity: T) =
        database.getCollection(collectionName).insertOne(entity!!.doc)

    fun register(id: Int, currency: Currency): Observable<String> {
        val user = User(id, currency)
        insert(COLLECTION_USER_NAME, user)
        return Observable.just("Registered: $user")
    }

    fun add(name: String, price: Double, currency: Currency): Observable<String> {
        val item = Item(name, price, currency)
        insert(COLLECTION_ITEM_NAME, item)
        return Observable.just("Added: $item")
    }

    private fun getUser(id: Int): Observable<User> =
        users.find(Filters.eq(User.PARAM_USER_ID, id)).toObservable().map { document: Document -> User(document) }

    fun getAll(id: Int): Observable<String> =
        getUser(id).map { obj: User -> obj.currency }.flatMap { currency: Currency ->
            items.map { item: Item -> "${item.name}: ${Currency.convert(item.currency, currency, item.price)}" }
        }
}