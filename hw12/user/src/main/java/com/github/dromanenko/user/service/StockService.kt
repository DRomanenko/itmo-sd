package com.github.dromanenko.user.service

import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

class StockService(
    private val host: String,
    private val port: Int,
    private val client: HttpClient = HttpClient.newHttpClient()
) {
    private fun doRequest(request: HttpRequest): HttpResponse<String>? =
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        }

    private fun getRequest(uri: String): HttpResponse<String>? =
        try {
            doRequest(HttpRequest.newBuilder(URI(uri)).GET().build())
        } catch (e: URISyntaxException) {
            throw IllegalStateException(e.message)
        }

    private fun postRequest(uri: String): HttpResponse<String>? =
        try {
            doRequest(HttpRequest.newBuilder(URI(uri)).POST(BodyPublishers.noBody()).build())
        } catch (e: URISyntaxException) {
            throw IllegalStateException(e.message)
        }

    private fun request(vararg args: Any): String {
        val builder = StringBuilder("$host:$port/exchange")
        args.forEach { arg -> builder.append(arg) }
        return builder.toString()
    }

    private fun getInteger(response: HttpResponse<String>?): Int = when {
        response!!.statusCode() == 200 -> response.body().toInt()
        else -> -1
    }

    private fun getDouble(response: HttpResponse<String>?): Double = when {
        response!!.statusCode() == 200 -> response.body().toDouble()
        else -> -1.0
    }

    private fun getBoolean(response: HttpResponse<String>): Boolean = when {
        response.statusCode() == 200 -> java.lang.Boolean.parseBoolean(response.body())
        else -> false
    }

    fun getCompanyStockPrice(companyId: Int): Double =
        getDouble(getRequest(request("/get_current_stock_price", "/", companyId)))

    fun getCurrentStockAmount(companyId: Int): Int =
        getInteger(getRequest(request("/get_current_stocks_amount", "/", companyId)))

    fun buyCompanyStocks(companyId: Int, amount: Int): Boolean =
        postRequest(
            request(
                "/buy_company_stock",
                "?companyId=", companyId,
                "&amount=", amount
            )
        )!!.statusCode() == 200

    fun sellCompanyStocksInAmount(companyId: Int, amount: Int): Boolean =
        postRequest(
            request(
                "/sell_company_stock",
                "?companyId=", companyId,
                "&amount=", amount
            )
        )!!.statusCode() == 200

    fun changeStockPrice(companyId: Int, delta: Double): Boolean =
        postRequest(
            request(
                "/change_company_price",
                "?companyId=", companyId,
                "&delta=", delta
            )
        )!!.statusCode() == 200

    fun addNewCompany(): Int = getInteger(postRequest(request("/add_new_company")))


    fun addStocks(companyId: Int, amount: Int): Boolean =
        postRequest(
            request(
                "/add_stock_in_company",
                "?companyId=", companyId,
                "&amount=", amount
            )
        )!!.statusCode() == 200
}