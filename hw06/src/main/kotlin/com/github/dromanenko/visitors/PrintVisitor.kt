package com.github.dromanenko.visitors

import com.github.dromanenko.token.*
import java.io.OutputStream

class PrintVisitor(outputStream: OutputStream) : TokenVisitor<Unit> {
    private val writer = outputStream.bufferedWriter()

    override fun visit(number: NumberToken) {
        writer.write("$number ")
    }

    override fun visit(brace: Brace) {
        writer.write("$brace ")
    }

    override fun visit(operation: Operation) {
        writer.write("$operation ")
    }

    override fun visit(tokens: List<Token>) {
        tokens.forEach { it.accept(this) }
        writer.flush()
    }
}