package com.github.dromanenko.entity

import org.bson.Document
import com.github.dromanenko.util.Currency
import com.github.dromanenko.util.Currency.Companion.PARAM_CURRENCY

class Item(val name: String, val price: Double, val currency: Currency) : BaseEntity {
    companion object {
        const val PARAM_ITEM_NAME = "name"
        const val PARAM_ITEM_PRICE = "price"
    }

    constructor(document: Document) : this(
        document.getString(PARAM_ITEM_NAME),
        document.getDouble(PARAM_ITEM_PRICE),
        Currency.getCurrencyFromString(document.getString(PARAM_CURRENCY))
    )

    override val doc: Document
        get() = Document().append("name", name).append("price", price).append("currency", currency)

    override fun toString(): String = "Item{name='$name', price=$price, currency=$currency}"
}