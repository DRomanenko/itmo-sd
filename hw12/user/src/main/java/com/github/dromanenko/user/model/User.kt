package com.github.dromanenko.user.model

class User(
    private val id: Int,
    var money: Double,
    val role: Role
) {
    val stocks: MutableList<Stock> = ArrayList()

    constructor(id: Int) : this(id, 0.0, Role.USER)

    fun putMoney(amount: Double) {
        money += amount
    }

    fun findStock(stockId: Int): Stock? = stocks.firstOrNull { it.id == stockId }

    fun addStock(stockId: Int, amount: Int) =
        findStock(stockId)?.put(amount) ?: stocks.add(Stock(stockId, amount))
}