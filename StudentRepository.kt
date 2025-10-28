package com.example.studentsportfolio.data

import kotlinx.coroutines.flow.Flow

class StudentRepository(private val dao: StudentDao) {
    val allStudents: Flow<List<Student>> = dao.getAll()

    suspend fun insert(student: Student) = dao.insert(student)
    suspend fun update(student: Student) = dao.update(student)
    suspend fun delete(student: Student) = dao.delete(student)
}
