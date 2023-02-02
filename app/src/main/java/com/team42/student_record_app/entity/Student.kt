package com.team42.student_record_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_records")
data class Student(
    @ColumnInfo(name = "studentName")
    var studentName: String,
    @ColumnInfo(name = "rollNumber")
    var rollNumber: String,
    @ColumnInfo(name = "studentClass")
    var studentClass: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}
