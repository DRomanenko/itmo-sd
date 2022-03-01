package com.github.dromanenko.hw4.dao

import com.github.dromanenko.hw4.model.Task
import com.github.dromanenko.hw4.model.TaskList
import java.util.concurrent.atomic.AtomicInteger

class TaskInMemoryDao : TaskDao {
    private val lastTaskId = AtomicInteger(0)
    private val lastTaskListId = AtomicInteger(0)
    private val taskLists = mutableListOf<TaskList>()
    private val tasks = mutableListOf<Task>()

    override fun addTask(task: Task): Int {
        task.id = lastTaskId.incrementAndGet()
        tasks.add(task)
        return task.id
    }

    override fun addTaskList(taskList: TaskList): Int {
        taskList.id = lastTaskListId.incrementAndGet()
        taskLists.add(taskList)
        return taskList.id
    }


    override fun getTasks() = tasks.toList()

    override fun getTasks(listId: Int) = tasks.filter { it.listId == listId }.toList()


    override fun getTaskList(id: Int): TaskList = taskLists.single { it.id == id }

    override fun getTaskLists() = taskLists.toList()


    override fun setTaskStatus(taskId: Int, done: Boolean) {
        tasks.single { it.id == taskId }.done = done
    }


    override fun deleteTask(taskId: Int) {
        tasks.removeIf { it.id == taskId }
    }

    override fun deleteTasksInList(listId: Int) {
        tasks.removeIf { it.listId == listId }
    }

    override fun deleteTaskList(listId: Int) {
        taskLists.removeIf { it.id == listId }
    }
}