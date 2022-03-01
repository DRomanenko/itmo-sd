package com.github.dromanenko.token

import com.github.dromanenko.visitors.TokenVisitor

class NumberToken(val value: Int) : BaseToken(value.toString()) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visit(this)
}