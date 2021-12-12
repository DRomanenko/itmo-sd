package com.github.dromanenko.token

import com.github.dromanenko.visitors.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor<*>)
}