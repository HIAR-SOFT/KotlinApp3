package com.example.kotlinapp3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.kotlinapp3.data.Task
import com.example.kotlinapp3.data.TaskDatabase
import com.example.kotlinapp3.data.TaskRepository

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _taskCount = MutableStateFlow(0)
    val taskCount: StateFlow<Int> = _taskCount.asStateFlow()

    private val _completedTaskCount = MutableStateFlow(0)
    val completedTaskCount: StateFlow<Int> = _completedTaskCount.asStateFlow()

    var newTaskTitle by mutableStateOf("")
    var newTaskDescription by mutableStateOf("")

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)

        // Observe tasks from database
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }

        // Observe task count
        viewModelScope.launch {
            repository.getTaskCount().collect { count ->
                _taskCount.value = count
            }
        }

        // Observe completed task count
        viewModelScope.launch {
            repository.getCompletedTaskCount().collect { count ->
                _completedTaskCount.value = count
            }
        }


    }

    fun addTask(title: String, description: String = "") {
        if (title.isNotBlank()) {
            viewModelScope.launch {
                val newTask = Task(
                    title = title.trim(),
                    description = description.trim()
                )
                repository.insertTask(newTask)
            }
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                val updatedTask = it.copy(isCompleted = !it.isCompleted)
                repository.updateTask(updatedTask)
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTaskById(taskId)
        }
    }

    fun updateNewTaskTitle(title: String) {
        newTaskTitle = title
    }

    fun updateNewTaskDescription(description: String) {
        newTaskDescription = description
    }

    fun addNewTask() {
        addTask(newTaskTitle, newTaskDescription)
        newTaskTitle = ""
        newTaskDescription = ""
    }
}