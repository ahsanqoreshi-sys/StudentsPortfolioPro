package com.example.studentsportfolio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val grade: String,
    val rollNumber: String,
    val parentContact: String,
    val notes: String?,
    val photoUri: String?
)
