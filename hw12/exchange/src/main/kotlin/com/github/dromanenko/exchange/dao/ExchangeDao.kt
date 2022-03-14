package com.github.dromanenko.exchange.dao

interface ExchangeDao {
    companion object {
        const val MIN_STOCKS_PRICE = 0.1
    }

    fun addNewCompany(): Int

    fun addStockInCompany(companyId: Int, amount: Int): Boolean

    fun getCurrentStocksAmount(companyId: Int): Int

    fun getCurrentStockPrice(companyId: Int): Double

    fun buyCompanyStock(companyId: Int, amount: Int): Boolean

    fun sellCompanyStock(companyId: Int, amount: Int): Boolean

    fun changeCompanyPrice(companyId: Int, delta: Double): Boolean
}