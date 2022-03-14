package com.github.dromanenko.exchange.controller

import com.github.dromanenko.exchange.dao.ExchangeDao
import com.github.dromanenko.exchange.dao.ExchangeInMemoryDao
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exchange")
class ExchangeController(
    private val dao: ExchangeDao = ExchangeInMemoryDao()
) {
    private fun checkResult(result: Boolean): ResponseEntity<*> = when {
        result -> ResponseEntity.ok<Any?>(null)
        else -> ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/add_new_company")
    fun addNewCompany(): ResponseEntity<Int> = ResponseEntity.ok(dao.addNewCompany())

    @PostMapping("/add_stock_in_company")
    fun addStockInCompany(
        companyId: Int,
        amount: Int
    ): ResponseEntity<*> = checkResult(dao.addStockInCompany(companyId, amount))

    @GetMapping("/get_current_stocks_amount/{companyId}")
    fun getCurrentStocksAmount(
        @PathVariable companyId: Int
    ): ResponseEntity<Int> = ResponseEntity.ok(dao.getCurrentStocksAmount(companyId))

    @GetMapping("/get_current_stock_price/{companyId}")
    fun getCurrentStockPrice(
        @PathVariable companyId: Int
    ): ResponseEntity<Double> = ResponseEntity.ok(dao.getCurrentStockPrice(companyId))

    @PostMapping("/buy_company_stock")
    fun buyCompanyStock(
        companyId: Int, amount: Int
    ): ResponseEntity<*> = checkResult(dao.buyCompanyStock(companyId, amount))

    @PostMapping("/sell_company_stock")
    fun sellCompanyStock(
        companyId: Int,
        amount: Int
    ): ResponseEntity<*> = checkResult(dao.sellCompanyStock(companyId, amount))

    @PostMapping("/change_company_price")
    fun changeCompanyPrice(
        companyId: Int,
        delta: Double
    ): ResponseEntity<*> = checkResult(dao.changeCompanyPrice(companyId, delta))

}