package com.team42.student_record_app.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.repository.StudentRepository
import com.team42.student_record_app.room.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : ViewModel() {

    //creating a variable for all students list and repository
    private val allStudents: LiveData<List<Student>>
    private val repository: StudentRepository

    init {
        val dao = StudentDatabase.getDatabase(application).getStudentDatabaseDao()
        repository = StudentRepository(dao)
        allStudents = repository.allStudents
    }

    // Calling a method from repository to add a new student record.
    fun addStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(student)
    }

    // Calling a method from repository to update student record.
    fun updateStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(student)
    }

    // Calling a method from repository to delete student record.
    fun deleteStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(student)
    }
}