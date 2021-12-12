package com.github.dromanenko.visitors

import com.github.dromanenko.token.*

interface TokenVisitor<out T> {
    fun visit(number: NumberToken)

    fun visit(brace: Brace)

    fun visit(operation: Operation)

    fun visit(tokens: List<Token>): T
}