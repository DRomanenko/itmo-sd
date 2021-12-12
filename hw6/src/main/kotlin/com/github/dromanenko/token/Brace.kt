package com.github.dromanenko.token

import com.github.dromanenko.visitors.TokenVisitor

abstract class Brace(string: String) : BaseToken(string) {
    override fun accept(visitor: TokenVisitor<*>) = visitor.visit(this)
}

class OpenBrace : Brace("(")
class CloseBrace : Brace(")")