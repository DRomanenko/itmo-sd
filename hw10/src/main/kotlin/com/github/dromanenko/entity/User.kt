package com.github.dromanenko.entity

import org.bson.Document
import com.github.dromanenko.util.Currency
import com.github.dromanenko.util.Currency.Companion.PARAM_CURRENCY

class User(private val id: Int, val currency: Currency) : BaseEntity {
    companion object {
        const val PARAM_USER_ID = "id"
    }

    constructor(document: Document) : this(
        document.getInteger(PARAM_USER_ID), Currency.getCurrencyFromString(PARAM_CURRENCY)
    )

    override val doc: Document
        get() = Document().append("id", id).append("currency", currency)

    override fun toString(): String = "User{id=$id, currency=$currency}"
}