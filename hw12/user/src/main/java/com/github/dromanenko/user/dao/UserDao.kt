package com.github.dromanenko.user.dao

import com.github.dromanenko.user.model.Stock

interface UserDao {
    fun addNewUser(): Int

    fun putMoneyToUserWallet(userId: Int, amount: Double): Boolean

    fun getStocks(userId: Int): List<Stock?>?

    fun getUserMoney(userId: Int): Double

    fun tryBuyStockToUser(companyId: Int, amount: Int, userId: Int): Boolean

    fun sellStockToUser(companyId: Int, amount: Int, userId: Int): Boolean

    fun tryChangeStocksPrice(companyId: Int, delta: Double, userId: Int): Boolean
}