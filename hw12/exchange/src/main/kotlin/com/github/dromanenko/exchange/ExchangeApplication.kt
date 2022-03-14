package com.github.dromanenko.exchange

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ExchangeApplication {}

fun main(args: Array<String>) {
    SpringApplication.run(ExchangeApplication::class.java, *args)
}