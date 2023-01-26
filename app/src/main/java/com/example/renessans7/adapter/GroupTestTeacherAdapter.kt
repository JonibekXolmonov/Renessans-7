package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.databinding.ItemGroupTestTeacherBinding
import com.example.renessans7.models.test.Test

class GroupTestTeacherAdapter : ListAdapter<Test, GroupTestTeacherAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((String) -> Unit)
    lateinit var onTestVisibility: (position: Int, testId: String, visibility: Boolean) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Test>() {
        override fun areItemsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Test, newItem: Test): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemGroupTestTeacherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGroupTestTeacherBinding.inflate(
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
            swIsActive.isChecked = test.visible
            swIsActive.isClickable = false
            swIsActive.isEnabled = false

            switchView.setOnClickListener {
                onTestVisibility(position, test.testId, !test.visible)
            }

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

    fun changeVisibility(position: Int, visibility: Boolean) {
        currentList[position].visible = visibility
        notifyItemChanged(position)
    }
}