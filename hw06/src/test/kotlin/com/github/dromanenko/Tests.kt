package com.github.dromanenko

import com.github.dromanenko.Tests.TestTokens.`(`
import com.github.dromanenko.Tests.TestTokens.`)`
import com.github.dromanenko.Tests.TestTokens.`*`
import com.github.dromanenko.Tests.TestTokens.`+`
import com.github.dromanenko.Tests.TestTokens.`-`
import com.github.dromanenko.Tests.TestTokens.`|`
import com.github.dromanenko.Tests.TestTokens.toTokens
import com.github.dromanenko.token.*
import com.github.dromanenko.tokenizer.ImplTokenizer
import com.github.dromanenko.visitors.CalcVisitor
import com.github.dromanenko.visitors.ParserVisitor
import com.github.dromanenko.visitors.PrintVisitor
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class Tests {
    internal object TestTokens {
        val `(` get() = OpenBrace()
        val `)` get() = CloseBrace()
        val `+` get() = SumOperation()
        val `-` get() = SubOperation()
        val `*` get() = MulOperation()
        val `|` get() = DivOperation()

        fun List<Any>.toTokens(): List<Any> = this.map { if (it is Int) NumberToken(it) else it }
    }

    private lateinit var parserVisitor: ParserVisitor
    private lateinit var calcVisitor: CalcVisitor
    private lateinit var printVisitor: PrintVisitor

    @BeforeTest
    fun init() {
        parserVisitor = ParserVisitor()
        calcVisitor = CalcVisitor()
        printVisitor = PrintVisitor(System.out)
    }

    private fun parseString(str: String): List<Token> {
        return ImplTokenizer(str.byteInputStream()).toList()
    }

    @Test
    fun `correct - parse sum`() {
        assertEquals(parseString("2 + 2"), listOf(2, `+`, 2).toTokens())
    }

    @Test
    fun `correct - parse sub`() {
        assertEquals(parseString("1    -   2  "), listOf(1, `-`, 2).toTokens())
    }

    @Test
    fun `correct - parse mul`() {
        assertEquals(parseString("    2     *   2"), listOf(2, `*`, 2).toTokens())
    }

    @Test
    fun `correct - parse div`() {
        assertEquals(parseString(" 2    /  1   "), listOf(2, `|`, 1).toTokens())
    }

    @Test
    fun `correct - parse`() {
        assertEquals(
            parseString("1 - ((1220 / 20) * (99 - 12))"),
            listOf(1, `-`, `(`, `(`, 1220, `|`, 20, `)`, `*`, `(`, 99, `-`, 12, `)`, `)`).toTokens()
        )
    }

    @Test
    fun `correct - calculate`() {
        assertEquals(
            calcVisitor.visit(parserVisitor.visit(parseString("1 - ((1220 / 20) * (99 - 12))"))),
            -5306
        )
    }
}