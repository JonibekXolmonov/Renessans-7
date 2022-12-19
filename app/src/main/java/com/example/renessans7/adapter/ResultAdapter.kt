package com.example.renessans7.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemCorrectIncorrectBinding
import com.example.renessans7.databinding.ItemGroupListBinding
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.setStroke

class ResultAdapter : ListAdapter<Boolean, ResultAdapter.VH>(DiffUtil()) {

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Boolean>() {
        override fun areItemsTheSame(oldItem: Boolean, newItem: Boolean): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Boolean, newItem: Boolean): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemCorrectIncorrectBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemCorrectIncorrectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {
            Log.d("TAG", "onBindViewHolder:${currentList} ${currentList.size}")
            ivChecker.setImageResource(if (getItem(position)) R.drawable.ic_correct else R.drawable.ic_incorrect)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}