package com.team42.student_record_app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.team42.student_record_app.entity.Student

@Database(entities = [Student::class], version = 1, exportSchema = true)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun getStudentDatabaseDao(): StudentDAO

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE =
                    Room.databaseBuilder(context, StudentDatabase::class.java, "STUDENT_DB")
                        .fallbackToDestructiveMigration().build()
                return INSTANCE!!
            }

        }

    }
}