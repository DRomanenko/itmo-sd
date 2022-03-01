import com.github.dromanenko.database.MongoUtils
import com.github.dromanenko.entity.Item
import com.github.dromanenko.entity.User
import com.github.dromanenko.exceptions.MyException
import com.github.dromanenko.util.Currency
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import rx.Observable

private const val PORT = 8081

fun getParameter(key: String, parameters: Map<String, List<String>>): String {
    return parameters[key]?.get(0) ?: throw MyException("No parameter")
}

fun main() {
    val mongoUtils = MongoUtils()
    HttpServer.newServer(PORT)
        .start { request: HttpServerRequest<ByteBuf>, response: HttpServerResponse<ByteBuf> ->
            val query = request.decodedPath.substring(1)
            val parameters = request.queryParameters
            var message: Observable<String> = Observable.from(arrayOf())
            try {
                when (query) {
                    "register" -> {
                        val id: Int = getParameter(User.PARAM_USER_ID, parameters).toInt()
                        val currency: Currency =
                            Currency.getCurrencyFromString(getParameter(Currency.PARAM_CURRENCY, parameters))
                        message = mongoUtils.register(id, currency)
                    }
                    "add" -> {
                        val name = getParameter(Item.PARAM_ITEM_NAME, parameters)
                        val price = getParameter(Item.PARAM_ITEM_PRICE, parameters).toDouble()
                        val currency: Currency =
                            Currency.getCurrencyFromString(getParameter(Currency.PARAM_CURRENCY, parameters))
                        message = mongoUtils.add(name, price, currency)
                    }
                    "all" -> {
                        val id: Int = getParameter(User.PARAM_USER_ID, parameters).toInt()
                        message = mongoUtils.getAll(id)
                    }
                    else -> throw MyException("Unsupported: '$query'")
                }
            } catch (e: RuntimeException) {
                response.status = HttpResponseStatus.BAD_REQUEST
                response.writeString(Observable.just(e.message))
            }
            response.status = HttpResponseStatus.OK
            response.writeString(message)
        }.awaitShutdown()
}