package com.github.dromanenko.visitors

import java.util.*
import com.github.dromanenko.token.*

class CalcVisitor : TokenVisitor<Int> {
    private val stack = Stack<Int>()

    override fun visit(number: NumberToken) {
        stack.add(number.value)
    }

    override fun visit(brace: Brace) {}

    override fun visit(operation: Operation) {
        if (stack.size < 2) throw IllegalStateException("There are not enough arguments for the `$operation`")
        val right = stack.pop()
        val left = stack.pop()
        stack.add(
            when (operation) {
                is SumOperation -> left + right
                is SubOperation -> left - right
                is MulOperation -> left * right
                is DivOperation -> left / right
                else -> throw IllegalArgumentException("Illegal operation: `${operation.javaClass.name}`")
            }
        )
    }

    override fun visit(tokens: List<Token>): Int {
        stack.clear()
        tokens.forEach { it.accept(this) }
        if (stack.size < 1) throw IllegalStateException("Missing numbers")
        if (stack.size > 1) throw IllegalStateException("Not all operations are calculated")
        return stack.peek()
    }
}