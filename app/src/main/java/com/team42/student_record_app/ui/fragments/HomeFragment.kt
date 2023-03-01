package com.team42.student_record_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team42.student_record_app.R
import com.team42.student_record_app.adapters.StudentRVAdapter
import com.team42.student_record_app.databinding.FragmentHomeBinding
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.interfaces.ItemClickListener
import com.team42.student_record_app.prefmanager.PrefManager
import com.team42.student_record_app.repository.StudentRepository
import com.team42.student_record_app.room.StudentDatabase
import com.team42.student_record_app.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ItemClickListener {

    private lateinit var viewModel: StudentViewModel
    private lateinit var studentDatabase: StudentDatabase
    private lateinit var repository: StudentRepository

    private var binding: FragmentHomeBinding? = null
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
        studentDetails = PrefManager.studentDetails
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentHomeBinding
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"
        super.onViewCreated(view, savedInstanceState)
        val studentRVAdapter = context?.let { StudentRVAdapter(it, this@HomeFragment) }
        view.requestApplyInsets()
        binding?.homeFragment = this
        binding?.apply {
            rcvHome.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        when {
                            dy > 0 && addFab.visibility == View.VISIBLE -> addFab.hide()
                            dy < 0 && addFab.visibility != View.VISIBLE -> addFab.show()
                        }
                    }
                })
            }
            lifecycleOwner = viewLifecycleOwner
        }
        viewModel.allStudents.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding?.emptyLayout?.isVisible = true
                binding?.rcvHome?.isVisible = false
            } else {
                binding?.emptyLayout?.isVisible = false
                binding?.rcvHome?.isVisible = true
                binding?.apply {
                    rcvHome.apply {
                        studentRVAdapter?.updateList(it)
                        adapter = studentRVAdapter

                    }
                }
            }
        }
    }

    fun addNewRecord() {
        PrefManager.fromWhere = 1
        findNavController().navigate(R.id.action_homeFragment_to_addRecordFragment)
    }

    override fun onStudentItemClicked(student: Student) {
        PrefManager.studentDetails = student
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
    }

    override fun onEditButtonClicked(student: Student) {
        PrefManager.fromWhere = 2
        PrefManager.studentDetails = student
        findNavController().navigate(R.id.action_homeFragment_to_addRecordFragment)
    }

    override fun onDeleteButtonClicked(student: Student) {
        val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
        val alert = builder?.apply {
            setTitle(R.string.delete_title)
            setMessage(R.string.delete_message)
            setCancelable(false)
            setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                viewModel.deleteStudent(student)
                Toast.makeText(
                    requireContext(),
                    "Student ${student.studentName} Deleted",
                    Toast.LENGTH_LONG
                ).show()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }?.create()
        alert?.show()
    }
}