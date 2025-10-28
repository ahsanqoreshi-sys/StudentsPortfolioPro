package com.example.studentsportfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studentsportfolio.data.Student
import com.example.studentsportfolio.data.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    val allStudents: StateFlow<List<Student>> = repository.allStudents
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun insert(student: Student) = viewModelScope.launch { repository.insert(student) }
    fun update(student: Student) = viewModelScope.launch { repository.update(student) }
    fun delete(student: Student) = viewModelScope.launch { repository.delete(student) }
}

class StudentViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
