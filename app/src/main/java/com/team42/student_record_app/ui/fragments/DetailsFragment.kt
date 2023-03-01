package com.team42.student_record_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.team42.student_record_app.R
import com.team42.student_record_app.databinding.FragmentDetailsBinding
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.prefmanager.PrefManager
import com.team42.student_record_app.repository.StudentRepository
import com.team42.student_record_app.room.StudentDatabase
import com.team42.student_record_app.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private lateinit var viewModel: StudentViewModel
    private lateinit var studentDatabase: StudentDatabase
    private lateinit var repository: StudentRepository

    private var binding: FragmentDetailsBinding? = null
    private var studentDetails: Student? = null

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
        val fragmentBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Student Details"
        studentDetails = PrefManager.studentDetails
        view.requestApplyInsets()
        binding?.detailsFragment = this
        binding?.apply {
            txtStudentName.text = studentDetails?.studentName
            txtRollNumber.text = studentDetails?.rollNumber
            txtClass.text = studentDetails?.studentClass
            txtMobileNumber.text = studentDetails?.mobileNumber

            saveButton.setOnClickListener {
                PrefManager.fromWhere = 2
                PrefManager.studentDetails = studentDetails!!
                findNavController().navigate(R.id.action_detailsFragment_to_addRecordFragment)
            }
            deleteButton.setOnClickListener {
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                val alert = builder?.apply {
                    setTitle(R.string.delete_title)
                    setMessage(R.string.delete_message)
                    setCancelable(false)
                    setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        lifecycleScope.launch {
                            studentDetails?.let { it1 -> viewModel.deleteStudent(it1) }
                            Toast.makeText(
                                requireContext(),
                                "Student ${studentDetails?.studentName} Deleted",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()

                        }

                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                }?.create()
                alert?.show()
            }
        }

    }
}