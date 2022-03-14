package com.github.dromanenko.user

import com.github.dromanenko.user.dao.UserInMemoryDao
import com.github.dromanenko.user.service.StockService
import org.junit.ClassRule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class UserApplicationTests {
    companion object {
        @ClassRule
        var simpleWebServer: GenericContainer<*> = FixedHostPortGenericContainer("exchange:1.0-SNAPSHOT")
            .withFixedExposedPort(8081, 8081)
            .withExposedPorts(8081)
        private val service = StockService("http://localhost", 8081)
        private val userInMemoryDao = UserInMemoryDao(service)

        @Test
        @BeforeAll
        fun `extra add new company`() =
            assertEquals(1, service.addNewCompany())
    }


    @Test
    @BeforeEach
    fun `test StockService - correct add and get`() {
        val companyId = service.addNewCompany()
        assertTrue(service.addStocks(companyId, 38))
        assertEquals(38, service.getCurrentStockAmount(companyId))
        assertEquals(0.1, service.getCompanyStockPrice(companyId))
    }

    @Test
    fun `test - correct change`() {
        assertEquals(0.1, service.getCompanyStockPrice(0))
        assertTrue(userInMemoryDao.tryChangeStocksPrice(0, 150.9, 0))
        assertEquals(151.0, service.getCompanyStockPrice(0))
    }

    @Test
    fun `test - user account`() {
        assertEquals(1, userInMemoryDao.addNewUser())
        assertTrue(userInMemoryDao.putMoneyToUserWallet(1, 100.0))
        assertTrue(userInMemoryDao.tryChangeStocksPrice(0, -10.0, 0))
        assertFalse(userInMemoryDao.tryBuyStockToUser(0, 10, 1))
        assertEquals(100.0, userInMemoryDao.getUserMoney(1))
        assertFalse(userInMemoryDao.tryChangeStocksPrice(0, 1.9, 1))
    }
}