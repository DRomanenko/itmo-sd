package com.github.dromanenko.user.model

class Stock(val id: Int, var amount: Int) {
    fun put(amount: Int) {
        this.amount += amount
        assert(this.amount >= 0)
    }
}