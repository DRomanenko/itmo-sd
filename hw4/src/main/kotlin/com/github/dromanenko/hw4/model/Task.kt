package com.github.dromanenko.hw4.model

data class Task(
    var id: Int = 0,
    var listId: Int = 0,
    var title: String = "",
    var description: String = "",
    var done: Boolean = false
)