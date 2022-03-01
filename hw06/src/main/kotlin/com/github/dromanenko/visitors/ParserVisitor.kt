package com.github.dromanenko.visitors

import java.util.*
import com.github.dromanenko.token.*

class ParserVisitor : TokenVisitor<List<Token>> {
    private val stack = Stack<Token>()
    private val result = mutableListOf<Token>()

    override fun visit(number: NumberToken) {
        result.add(number)
    }

    override fun visit(brace: Brace) {
        when (brace) {
            is OpenBrace -> stack.add(brace)
            is CloseBrace -> {
                if (stack.isEmpty()) throw IllegalStateException("Opening bracket is missing")
                var currentSymbol = stack.pop()
                while (currentSymbol !is OpenBrace) {
                    result.add(currentSymbol)
                    if (stack.isEmpty()) throw IllegalStateException("Opening bracket is missing")
                    currentSymbol = stack.pop()
                }
            }
        }
    }

    override fun visit(operation: Operation) {
        if (stack.isNotEmpty()) {
            var currentSymbol = stack.peek()
            while (currentSymbol is Operation && currentSymbol.priority >= operation.priority) {
                result.add(stack.pop())
                if (stack.isEmpty()) break
                currentSymbol = stack.peek()
            }
        }
        stack.add(operation)
    }

    override fun visit(tokens: List<Token>): List<Token> {
        result.clear()
        tokens.forEach { it.accept(this) }
        while (stack.isNotEmpty()) result.add(stack.pop())
        return result.toList()
    }
}