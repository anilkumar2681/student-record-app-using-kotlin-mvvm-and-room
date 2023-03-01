package com.team42.student_record_app.interfaces

import com.team42.student_record_app.entity.Student

interface ItemClickListener {
    fun onStudentItemClicked(student: Student)
    fun onEditButtonClicked(student: Student)
    fun onDeleteButtonClicked(student: Student)
}