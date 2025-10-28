package com.example.studentsportfolio

import android.app.Application
import com.example.studentsportfolio.data.AppDatabase
import com.example.studentsportfolio.data.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StudentsApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { StudentRepository(database.studentDao()) }
}
