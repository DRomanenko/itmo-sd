package com.github.dromanenko.exchange.dao

class ExchangeInMemoryDao(
    private val amounts: MutableList<Int> = ArrayList(),
    private val prices: MutableList<Double> = ArrayList()
) : ExchangeDao {
    private fun checkSizes() {
        assert(amounts.size == prices.size)
    }

    private fun isCompanyNotExists(companyId: Int): Boolean {
        return companyId >= amounts.size
    }

    override fun addNewCompany(): Int {
        checkSizes()
        amounts.add(0)
        prices.add(ExchangeDao.MIN_STOCKS_PRICE)
        return amounts.size - 1
    }

    override fun addStockInCompany(companyId: Int, amount: Int): Boolean {
        checkSizes()
        if (isCompanyNotExists(companyId)) {
            return false
        }
        amounts[companyId] = amounts[companyId] + amount
        return true
    }

    override fun getCurrentStocksAmount(companyId: Int): Int {
        checkSizes()
        return if (isCompanyNotExists(companyId)) -1 else amounts[companyId]
    }

    override fun getCurrentStockPrice(companyId: Int): Double {
        checkSizes()
        return if (isCompanyNotExists(companyId)) {
            -1.0
        } else prices[companyId]
    }

    override fun buyCompanyStock(companyId: Int, amount: Int): Boolean {
        checkSizes()
        if (isCompanyNotExists(companyId) || amount < 0 || amounts[companyId] < amount) {
            return false
        }
        amounts[companyId] = amounts[companyId] - amount
        return true
    }

    override fun sellCompanyStock(companyId: Int, amount: Int): Boolean {
        checkSizes()
        if (isCompanyNotExists(companyId) || amount < 0) {
            return false
        }
        amounts[companyId] = amounts[companyId] + amount
        return false
    }

    override fun changeCompanyPrice(companyId: Int, delta: Double): Boolean {
        checkSizes()
        if (isCompanyNotExists(companyId)) {
            return false
        }
        val result = prices[companyId] + delta
        prices[companyId] = if (result <= 0) ExchangeDao.MIN_STOCKS_PRICE else result
        return true
    }
}