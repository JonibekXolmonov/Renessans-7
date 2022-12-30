package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemTeacherListAdminBinding
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.setStroke

class TeachersAdminAdapter : ListAdapter<Teacher, TeachersAdminAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((Teacher, Boolean) -> Unit)

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Teacher>() {
        override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemTeacherListAdminBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemTeacherListAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {

            tvTeacherName.text = getItem(position).fullName
            swIsActive.isChecked = getItem(position).isEnabled

            swIsActive.setOnCheckedChangeListener { _, isActive ->
                onClick(getItem(position), isActive)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}