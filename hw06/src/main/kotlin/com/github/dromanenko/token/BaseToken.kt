package com.github.dromanenko.token

abstract class BaseToken(private val string: String) : Token {
    override fun toString() = string

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?) = other is Token && toString() == other.toString()
}