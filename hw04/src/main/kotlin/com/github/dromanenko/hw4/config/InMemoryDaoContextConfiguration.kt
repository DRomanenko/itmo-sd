package com.github.dromanenko.hw4.config

import com.github.dromanenko.hw4.dao.TaskDao
import com.github.dromanenko.hw4.dao.TaskInMemoryDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InMemoryDaoContextConfiguration {
    @Bean
    fun productDao(): TaskDao {
        return TaskInMemoryDao()
    }
}