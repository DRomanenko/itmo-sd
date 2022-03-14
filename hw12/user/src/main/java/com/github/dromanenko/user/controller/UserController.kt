package com.github.dromanenko.user.controller

import com.github.dromanenko.user.dao.UserDao
import com.github.dromanenko.user.dao.UserInMemoryDao
import com.github.dromanenko.user.service.StockService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val service: StockService = StockService("http://localhost", 8081),
    private val dao: UserDao = UserInMemoryDao(service)
) {
    private fun checkResult(result: Boolean): ResponseEntity<*> = when {
        result -> ResponseEntity.ok<Any?>(null)
        else -> ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/add_new_user")
    fun addNewUser(): ResponseEntity<Int> = ResponseEntity.ok(dao.addNewUser())


    @PostMapping("/put_money_to_user_wallet")
    fun putMoneyToUserWallet(
        userId: Int,
        amount: Double
    ): ResponseEntity<*> =
        checkResult(dao.putMoneyToUserWallet(userId, amount))

    @GetMapping("/get_stocks/{userId}")
    fun getStocks(
        @PathVariable userId: Int
    ): ResponseEntity<*> {
        val stocks = dao.getStocks(userId)
        return if (stocks != null) {
            ResponseEntity.ok(stocks)
        } else ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/get_total_amount/{userId}")
    fun getTotalAmount(
        @PathVariable userId: Int
    ): ResponseEntity<*> = when (val sum = dao.getUserMoney(userId)) {
        -1.0 -> ResponseEntity<Any>(HttpStatus.BAD_REQUEST)
        else -> ResponseEntity.ok(sum)
    }

    @PostMapping("/try_buy_stock_to_user")
    fun tryBuyStockToUser(
        userId: Int,
        companyId: Int,
        amount: Int
    ): ResponseEntity<*> = checkResult(dao.tryBuyStockToUser(companyId, amount, userId))

    @PostMapping("/sell_stock_to_user")
    fun sellStockToUser(
        userId: Int,
        companyId: Int,
        amount: Int
    ): ResponseEntity<*> = checkResult(dao.sellStockToUser(companyId, amount, userId))
}