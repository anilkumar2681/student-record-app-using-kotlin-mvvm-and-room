package com.team42.student_record_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.team42.student_record_app.R
import com.team42.student_record_app.databinding.FragmentAddRecordBinding
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.extentions.isNotNullOrEmpty
import com.team42.student_record_app.extentions.toEditable
import com.team42.student_record_app.prefmanager.PrefManager
import com.team42.student_record_app.repository.StudentRepository
import com.team42.student_record_app.room.StudentDatabase
import com.team42.student_record_app.viewmodel.StudentViewModel


class AddRecordFragment : Fragment() {
    companion object {
        private const val edittextError = "This field is required"
    }

    private lateinit var viewModel: StudentViewModel
    private lateinit var studentDatabase: StudentDatabase
    private lateinit var repository: StudentRepository

    var studentName: String = ""
    var studentRollNumber: String = ""
    var studentClass: String = ""
    var studentMobile: String = ""

    private var binding: FragmentAddRecordBinding? = null
    private val listStudentClass =
        arrayOf(
            "1st",
            "2nd",
            "3rd",
            "4th",
            "5th",
            "6th",
            "7th",
            "8th",
            "9th",
            "10th",
            "11th",
            "12th"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        studentDatabase = context?.let { StudentDatabase.getDatabase(it) }!!
        repository = StudentRepository(studentDatabase.getStudentDatabaseDao())
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                requireActivity().application
            )
        )[StudentViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentAddRecordBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.apply {
            val fromWhere = PrefManager.fromWhere
            val savedStudentDetails = PrefManager.studentDetails

            if (fromWhere == 1) {
                (requireActivity() as AppCompatActivity).supportActionBar?.title = "Add Student Record"
                spinnerClass.adapter = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        listStudentClass
                    )
                }
                spinnerClass.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        studentClass = parent?.getItemAtPosition(position).toString()

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        //
                    }
                }
                saveButton.setOnClickListener {
                    studentName = edtStudentName.text.toString()
                    studentRollNumber = edtRollNumber.text.toString()
                    studentMobile = edtMobileNumber.text.toString()
                    val student = Student(
                        studentName,
                        studentRollNumber,
                        studentClass,
                        studentMobile,
                    )
                    PrefManager.studentDetails = student

                    if (edtStudentName.isNotNullOrEmpty(edittextError)
                        and (edtRollNumber.isNotNullOrEmpty(edittextError))
                    ) {
                        if (edtMobileNumber.isNotNullOrEmpty(edittextError)) {
                            viewModel.addStudent(student)
                            Toast.makeText(
                                context,
                                "${student.studentName} Record Added Successfully!!",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                    }

                }


            } else {
                (requireActivity() as AppCompatActivity).supportActionBar?.title = "Update Student Record"
                edtRollNumber.background = requireActivity().getDrawable(R.drawable.txt_background)
                edtRollNumber.tag = edtRollNumber.keyListener
                edtRollNumber.keyListener = null
                spinnerClass.adapter = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        listStudentClass
                    )
                }
                spinnerClass.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        studentClass = parent?.getItemAtPosition(position).toString()

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        //
                    }
                }
                spinnerClass.setSelection(listStudentClass.indexOf(savedStudentDetails.studentClass))
                edtStudentName.text = savedStudentDetails.studentName.toEditable()
                edtRollNumber.text = savedStudentDetails.rollNumber.toEditable()
                edtMobileNumber.text = savedStudentDetails.mobileNumber.toEditable()
                saveButton.setOnClickListener {
                    studentName = edtStudentName.text.toString()
                    studentRollNumber = edtRollNumber.text.toString()
                    studentMobile = edtMobileNumber.text.toString()
                    val student = Student(
                        studentName,
                        studentRollNumber,
                        studentClass,
                        studentMobile,
                    )
                    PrefManager.studentDetails = student

                    if (edtStudentName.isNotNullOrEmpty(edittextError)
                        and (edtRollNumber.isNotNullOrEmpty(edittextError))
                    ) {
                        if (edtMobileNumber.isNotNullOrEmpty(edittextError)) {
                            viewModel.updateStudent(student)
                            Toast.makeText(
                                context,
                                "${student.studentName} Record Updated Successfully!!",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                    }

                }
            }
        }
    }
}