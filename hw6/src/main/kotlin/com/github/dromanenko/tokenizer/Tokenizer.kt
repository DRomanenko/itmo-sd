package com.github.dromanenko.tokenizer

import com.github.dromanenko.token.Token

interface Tokenizer {
    fun next(): Token?

    fun toList(): List<Token>
}