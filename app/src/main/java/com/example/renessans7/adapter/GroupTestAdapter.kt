package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemGroupListBinding
import com.example.renessans7.databinding.ItemGroupTestBinding
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.models.test.Test
import com.example.renessans7.utils.setStroke
import java.text.SimpleDateFormat

class GroupTestAdapter : ListAdapter<Test, GroupTestAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((String) -> Unit)

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Test>() {
        override fun areItemsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemGroupTestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGroupTestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val test = getItem(position)
        holder.binding.apply {

            tvTestName.text = test.testName
            tvTestQuestionNumber.text = test.numberOfQuestions.toString()
            tvTestDate.text =
                "Date: ${test.testDate}"


            root.setOnClickListener {
                onClick.invoke(getItem(position).testId)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addGroup(test: Test) {
        currentList.add(test)
//        submitList(currentList)
    }
}