package com.github.dromanenko.entity

import org.bson.Document

interface BaseEntity {
    val doc: Document?
}