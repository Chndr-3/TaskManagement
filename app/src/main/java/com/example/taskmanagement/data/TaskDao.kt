package com.example.taskmanagement.data

import android.opengl.Visibility
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    suspend fun getAllTask() : List<Task>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task : Task)

    @Delete
    suspend fun deleteTask(task : Task)

    @Query("UPDATE task SET visibility = :visibility WHERE taskId = :id")
    suspend fun update(id: Int,visibility: Boolean)
}