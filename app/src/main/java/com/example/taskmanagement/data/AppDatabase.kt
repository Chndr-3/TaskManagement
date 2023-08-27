package com.example.taskmanagement.data

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

}

