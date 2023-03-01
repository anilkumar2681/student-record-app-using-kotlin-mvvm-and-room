package com.team42.student_record_app.prefmanager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.team42.student_record_app.entity.Student
import org.json.JSONObject

private const val FROM_WHERE = "from_where"
private const val STUDENT_DETAIL = "student_detail"

object PrefManager {

    private const val NAME = "student_pref"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var fromWhere: Int
        get() = preferences.getInt(FROM_WHERE, 1)
        set(value) {
            preferences.edit {
                it.putInt(FROM_WHERE, value)
            }
        }

    var studentDetails: Student
        get() {
            val gson = Gson()
            val emptyJson = JSONObject()
            val inString = preferences.getString(STUDENT_DETAIL, emptyJson.toString())
            return gson.fromJson(inString, Student::class.java)
        }
        set(value) {
            val gson = Gson()
            val studentDetailInString = gson.toJson(value)
            preferences.edit { it.putString(STUDENT_DETAIL, studentDetailInString) }
        }

}