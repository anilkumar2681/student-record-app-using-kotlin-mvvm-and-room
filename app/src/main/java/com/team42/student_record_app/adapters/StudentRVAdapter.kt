package com.team42.student_record_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team42.student_record_app.databinding.CustomRowItemBinding
import com.team42.student_record_app.entity.Student
import com.team42.student_record_app.interfaces.ItemClickListener

class StudentRVAdapter(
    private val context: Context,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<StudentRVAdapter.ViewHolder>() {

    private val allStudents = ArrayList<Student>()

    private lateinit var binding: CustomRowItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CustomRowItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount() = allStudents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(allStudents[position])
        binding.apply {
            imgEdit.setOnClickListener {
                itemClickListener.onEditButtonClicked(allStudents[position])
            }

            imgDelete.setOnClickListener {
                itemClickListener.onDeleteButtonClicked(allStudents[position])
            }

            rlMain.setOnClickListener {
                itemClickListener.onStudentItemClicked(allStudents[position])
            }
        }
        holder.setIsRecyclable(true)
    }

    fun updateList(newList: List<Student>) {
        allStudents.clear()
        allStudents.addAll(newList)
        notifyDataSetChanged()
    }


    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun setData(item: Student) {
            binding.apply {
                txtName.text = item.studentName
                txtRollNO.text = item.rollNumber
                txtClass.text = item.studentClass
                txtMobileNO.text = item.mobileNumber
            }
        }

    }
}