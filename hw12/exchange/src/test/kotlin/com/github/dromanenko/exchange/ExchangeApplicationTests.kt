package com.github.dromanenko.exchange

import com.github.dromanenko.exchange.dao.ExchangeDao
import com.github.dromanenko.exchange.dao.ExchangeInMemoryDao
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExchangeApplicationTests {
    companion object {
        const val COMPANY_STOCK_PRICE = 1000
    }

    @Test
    fun `test ExchangeInMemoryDao - correct add and get`() {
        val exchangeDao: ExchangeDao = ExchangeInMemoryDao()
        exchangeDao.addNewCompany()
        assertTrue(exchangeDao.addStockInCompany(0, COMPANY_STOCK_PRICE))
        assertEquals(COMPANY_STOCK_PRICE, exchangeDao.getCurrentStocksAmount(0))
        assertEquals(ExchangeDao.MIN_STOCKS_PRICE, exchangeDao.getCurrentStockPrice(0))
        assertEquals(-1, exchangeDao.getCurrentStocksAmount(1))
        assertEquals(-1.0, exchangeDao.getCurrentStockPrice(1))
    }
}