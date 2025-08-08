package com.example.kotlinapp3.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTaskCount(): Flow<Int> = taskDao.getTaskCount()

    fun getCompletedTaskCount(): Flow<Int> = taskDao.getCompletedTaskCount()

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteTaskById(id: Int) {
        taskDao.deleteTaskById(id)
    }

    suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)
    }
}
