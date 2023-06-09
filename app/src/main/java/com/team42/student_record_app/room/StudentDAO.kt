package com.team42.student_record_app.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.team42.student_record_app.entity.Student

@Dao
interface StudentDAO {
    // below is the insert method for adding a new student record to our database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student)

    // below is the delete method for deleting student record from data base
    @Delete
    suspend fun delete(student: Student)

    // below is the method to read all the students records by roll number order
    @Query("SELECT * from student_records order by rollNumber")
    fun getAllStudents(): LiveData<List<Student>>

    // below method is use to update the student record.
    @Update
    suspend fun update(student: Student)
}
