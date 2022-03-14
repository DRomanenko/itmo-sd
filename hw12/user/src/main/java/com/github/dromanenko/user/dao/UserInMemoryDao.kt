package com.github.dromanenko.user.dao

import com.github.dromanenko.user.model.Role
import com.github.dromanenko.user.model.Stock
import com.github.dromanenko.user.model.User
import com.github.dromanenko.user.service.StockService

class UserInMemoryDao(
    private val service: StockService,
    private val users: MutableList<User> = ArrayList()
) : UserDao {
    init {
        users.add(User(0, Double.MAX_VALUE, Role.ADMIN))
    }

    override fun addNewUser(): Int {
        users.add(User(users.size))
        return users.size - 1
    }

    private fun isUserNotExists(userId: Int): Boolean = userId >= users.size

    override fun putMoneyToUserWallet(userId: Int, amount: Double): Boolean {
        if (isUserNotExists(userId)) {
            return false
        }
        users[userId].putMoney(amount)
        return true
    }

    override fun getStocks(userId: Int): List<Stock?>? =
        when {
            isUserNotExists(userId) -> null
            else -> users[userId].stocks
        }

    override fun getUserMoney(userId: Int): Double {
        if (isUserNotExists(userId)) {
            return -1.0
        }
        return users[userId].money + users[userId].stocks.sumOf { it.amount * service.getCompanyStockPrice(it.id) }
    }

    override fun tryBuyStockToUser(companyId: Int, amount: Int, userId: Int): Boolean {
        if (isUserNotExists(userId)) {
            return false
        }
        val user = users[userId]
        val price = service.getCompanyStockPrice(companyId)
        if (user.money < price * amount) {
            return false
        }
        if (service.buyCompanyStocks(companyId, amount)) {
            user.addStock(companyId, amount)
            user.putMoney(-price * amount)
            return true
        }
        return false
    }

    override fun sellStockToUser(companyId: Int, amount: Int, userId: Int): Boolean {
        if (isUserNotExists(userId)) {
            return false
        }
        val user = users[userId]
        val owns = user.findStock(companyId)
        if (owns!!.amount < amount) {
            return false
        }
        if (service.sellCompanyStocksInAmount(companyId, amount)) {
            user.addStock(companyId, -amount)
            user.putMoney(service.getCompanyStockPrice(companyId) * amount)
            return true
        }
        return false
    }

    override fun tryChangeStocksPrice(companyId: Int, delta: Double, userId: Int): Boolean {
        if (isUserNotExists(userId)) {
            return false
        }
        val user = users[userId]
        return when {
            user.role != Role.ADMIN -> false
            else -> service.changeStockPrice(companyId, delta)
        }
    }
}