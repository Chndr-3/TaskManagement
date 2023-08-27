package com.example.taskmanagement.data


import javax.inject.Inject


class TaskRepository @Inject constructor (private val taskDao: TaskDao) {
    suspend fun getAllTask(): List<Task> {
        return taskDao.getAllTask()
    }

    suspend fun insertTask(task: Task) {
        return taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        return taskDao.deleteTask(task)
    }
}