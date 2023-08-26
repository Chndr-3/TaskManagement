package com.example.taskmanagement.data

import androidx.lifecycle.MutableLiveData


class TaskRepository(private val taskDao: TaskDao) {
    suspend fun getAllTask(): List<Task> {
        return taskDao.getAllTask()
    }

    suspend fun insertTask(task: Task) {
        return taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        return taskDao.deleteTask(task)
    }

    suspend fun updateVisibility(id: Int, visibility: Boolean) {
        return taskDao.update(id, visibility)
    }

    companion object{
        private val INSTANCE : TaskRepository? = null

        fun getInstance(taskDao: TaskDao) : TaskRepository{
            return INSTANCE ?: synchronized(this){
                val instance = TaskRepository(taskDao)
                instance
            }
        }
    }
}