package com.team42.student_record_app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.team42.student_record_app.repository.StudentRepository

class StudentsViewModelFactory(
    private val repository: StudentRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(StudentRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.e("ViewModelError: ", e.message.toString())
        }
        return super.create(modelClass)
    }
}