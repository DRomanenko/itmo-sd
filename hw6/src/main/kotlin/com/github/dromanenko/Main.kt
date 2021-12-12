package com.github.dromanenko

import com.github.dromanenko.tokenizer.ImplTokenizer
import com.github.dromanenko.visitors.*

fun main() {
    try {
        val parserVisitor = ParserVisitor()
        val calcVisitor = CalcVisitor()
        val printVisitor = PrintVisitor(System.out)

        val input = readLine() ?: throw IllegalArgumentException("Entry is empty")
        val tokenizer = ImplTokenizer(input.byteInputStream())

        val reversePolishNotation = parserVisitor.visit(tokenizer.toList())

        print("Reverse Polish Notation: ")
        printVisitor.visit(reversePolishNotation)
        println()
        println("Calculation result: ${calcVisitor.visit(reversePolishNotation)}")
    } catch (e: Exception) {
        System.err.println(e.message)
    }
}