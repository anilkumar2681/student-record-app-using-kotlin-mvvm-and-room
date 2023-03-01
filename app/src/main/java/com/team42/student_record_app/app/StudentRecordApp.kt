package com.team42.student_record_app.app

import android.app.Application
import com.team42.student_record_app.prefmanager.PrefManager

class StudentRecordApp : Application() {
    override fun onCreate() {
        super.onCreate()
        PrefManager.init(applicationContext)
    }
}