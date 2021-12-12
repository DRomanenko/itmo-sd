package com.github.dromanenko.token

import com.github.dromanenko.visitors.TokenVisitor

abstract class Operation(string: String, val priority: Int) : BaseToken(string) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visit(this)
}

class SumOperation : Operation("+", 0)
class SubOperation : Operation("-", 0)
class MulOperation : Operation("*", 1)
class DivOperation : Operation("/", 1)