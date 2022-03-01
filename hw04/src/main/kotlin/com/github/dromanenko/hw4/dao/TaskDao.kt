package com.github.dromanenko.hw4.dao

import com.github.dromanenko.hw4.model.Task
import com.github.dromanenko.hw4.model.TaskList

interface TaskDao {
    fun addTask(task: Task): Int

    fun addTaskList(taskList: TaskList): Int
    

    fun getTasks(): List<Task>

    fun getTasks(listId: Int): List<Task>
    

    fun getTaskList(id: Int): TaskList

    fun getTaskLists(): List<TaskList>
    

    fun setTaskStatus(taskId: Int, done: Boolean)


    fun deleteTask(taskId: Int)

    fun deleteTasksInList(listId: Int)

    fun deleteTaskList(listId: Int)
}