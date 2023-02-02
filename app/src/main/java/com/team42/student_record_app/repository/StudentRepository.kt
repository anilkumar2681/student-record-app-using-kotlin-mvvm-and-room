package com.team42.student_record_app.repository

import androidx.lifecycle.LiveData
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.room.StudentDAO

class StudentRepository(private val studentDAO: StudentDAO) {

    // we are getting all the students record  from our DAO class.
    val allStudents: LiveData<List<Student>> = studentDAO.getAllStudents()

    // for adding the student record to our database.
    suspend fun insert(student: Student) {
        studentDAO.insert(student)
    }

    // for deleting student record from database.
    suspend fun delete(student: Student){
        studentDAO.delete(student)
    }

    // updating student record from database.
    suspend fun update(student: Student){
        studentDAO.update(student)
    }
}