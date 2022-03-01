package com.github.dromanenko.tokenizer

import java.io.InputStream
import com.github.dromanenko.token.*

class ImplTokenizer(data: InputStream) : Tokenizer {
    private enum class State {
        START,
        NUMBER,
        ERROR,
        END
    }

    private var state: State = State.START

    private val reader = data.bufferedReader()

    private var currentSymbol = nextNotEmptySymbol()

    private var numberInDecimal: Int? = null

    private fun nextNotEmptySymbol(): Char {
        var currentSymbol: Char
        do {
            currentSymbol = reader.read().toChar()
        } while (currentSymbol.isWhitespace())
        return currentSymbol
    }

    private fun toToken(currentSymbol: Char) = when (currentSymbol) {
        '(' -> OpenBrace()
        ')' -> CloseBrace()
        '+' -> SumOperation()
        '-' -> SubOperation()
        '*' -> MulOperation()
        '/' -> DivOperation()
        else -> throw IllegalArgumentException("Illegal character: $currentSymbol")
    }

    override fun next(): Token? {
        return when (state) {
            State.START -> {
                return when {
                    currentSymbol == '\uFFFF' -> {
                        state = State.END
                        return null
                    }
                    currentSymbol in setOf('(', ')', '+', '-', '*', '/') -> {
                        val token = toToken(currentSymbol)
                        currentSymbol = nextNotEmptySymbol()
                        return token
                    }
                    currentSymbol.isDigit() -> {
                        state = State.NUMBER
                        numberInDecimal = currentSymbol.digitToInt()
                        currentSymbol = nextNotEmptySymbol()
                        next()
                    }
                    else -> {
                        state = State.ERROR
                        throw IllegalArgumentException("Illegal input character: $currentSymbol")
                    }
                }
            }
            State.NUMBER -> {
                when {
                    currentSymbol.isDigit() -> {
                        numberInDecimal = numberInDecimal?.times(10)?.plus(currentSymbol.digitToInt())
                        currentSymbol = nextNotEmptySymbol()
                        next()
                    }
                    else -> {
                        state = State.START
                        val number = numberInDecimal!!
                        numberInDecimal = null
                        return NumberToken(number)
                    }
                }
            }
            State.ERROR -> throw IllegalArgumentException("Illegal symbol: $currentSymbol")
            State.END -> return null
        }
    }

    override fun toList(): List<Token> {
        val result = mutableListOf<Token>()
        var currentSymbol = next()
        while (currentSymbol != null) {
            result.add(currentSymbol)
            currentSymbol = next()
        }
        return result
    }
}