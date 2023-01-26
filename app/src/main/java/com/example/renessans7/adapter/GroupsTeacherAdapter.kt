package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.databinding.ItemGroupListTeacherBinding
import com.example.renessans7.models.group.Group

class GroupsTeacherAdapter : ListAdapter<Group, GroupsTeacherAdapter.VH>(DiffUtil()) {

    lateinit var onClick: (String, String) -> Unit
    lateinit var onEdit: (group: Group) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemGroupListTeacherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGroupListTeacherBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {

            tvGroupName.text = getItem(position).className
            tvGroupInfo.text = getItem(position).numberOfPupil

            root.setOnClickListener {
                onClick.invoke(getItem(position).classId, getItem(position).className)
            }

            ivGroupEdit.setOnClickListener {
                onEdit.invoke(getItem(position))
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