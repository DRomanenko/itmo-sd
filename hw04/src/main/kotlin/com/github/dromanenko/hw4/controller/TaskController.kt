package com.github.dromanenko.hw4.controller

import com.github.dromanenko.hw4.dao.TaskDao
import com.github.dromanenko.hw4.model.Task
import com.github.dromanenko.hw4.model.TaskList
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class TaskController(private var taskDao: TaskDao) {
    @GetMapping("/")
    fun index() = "redirect:/task-lists"

    @GetMapping("/task-list")
    fun taskList(@RequestParam listId: Int, map: ModelMap): String {
        map.addAttribute("tasks", taskDao.getTasks(listId))
        map.addAttribute("taskList", taskDao.getTaskList(listId))
        map.addAttribute("task", Task())
        return "task-list"
    }

    @GetMapping("/task-lists")
    fun taskLists(map: ModelMap): String? {
        val taskLists = taskDao.getTaskLists()
        val tasksFromTaskLists = taskDao.getTasks().groupBy { it.listId }.toMutableMap()
        taskLists.filterNot { tasksFromTaskLists.containsKey(it.id) }.forEach { tasksFromTaskLists[it.id] = listOf() }
        map.addAttribute("taskLists", taskLists)
        map.addAttribute("sizes", tasksFromTaskLists.map { it.key to it.value.size }.toMap())
        map.addAttribute(
            "dones",
            tasksFromTaskLists.map { it.key to it.value.filter { td -> td.done }.size }.toMap()
        )
        map.addAttribute("taskList", TaskList())
        return "index"
    }


    @PostMapping("/add-task")
    fun addTask(@ModelAttribute("task") task: Task): String {
        taskDao.addTask(task)
        return "redirect:/task-list?listId=${task.listId}"
    }

    @PostMapping("/add-task-list")
    fun addTaskList(@ModelAttribute("taskList") taskList: TaskList): String {
        taskDao.addTaskList(taskList)
        return "redirect:/task-lists"
    }


    @PostMapping("/delete-task")
    fun deleteTask(@ModelAttribute("id") taskId: Int, @ModelAttribute("listId") listId: Int): String {
        taskDao.deleteTask(taskId)
        return "redirect:/task-list?listId=${listId}"
    }

    @PostMapping("/delete-task-list")
    fun deleteTaskList(@ModelAttribute("id") listId: Int): String {
        taskDao.deleteTasksInList(listId)
        taskDao.deleteTaskList(listId)
        return "redirect:/task-lists"
    }


    @PostMapping("/task-done")
    fun taskDone(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("id") taskId: Int,
        @ModelAttribute("done") done: Boolean,
    ): String {
        taskDao.setTaskStatus(taskId, done)
        return "redirect:/task-list?listId=${listId}"
    }
}