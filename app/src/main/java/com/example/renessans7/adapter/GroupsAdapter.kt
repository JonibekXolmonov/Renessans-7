package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemGroupListBinding
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.setStroke

class GroupsAdapter : ListAdapter<Group, GroupsAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((String, String) -> Unit)
    private var lastSelectedPos = -1
    private var currentSelectedPos = -1

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemGroupListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemGroupListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {

            tvGroupName.text = getItem(position).className
            tvGroupInfo.text = getItem(position).numberOfPupil
            if (holder.adapterPosition == currentSelectedPos) root.setStroke(
                3,
                root.context.resources.getColor(R.color.main_red)
            )
            else root.strokeWidth = 0

            root.setOnClickListener {
                root.setStroke(3, root.context.resources.getColor(R.color.main_red))
                onClick.invoke(getItem(position).classId, getItem(position).className)
                lastSelectedPos = currentSelectedPos
                currentSelectedPos = holder.adapterPosition

                if (lastSelectedPos != -1) notifyItemChanged(lastSelectedPos)
            }
        }
    }

    fun setPosToDefault() {
        lastSelectedPos = -1
        currentSelectedPos = -1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addGroup(group: Group) {
        currentList.add(group)
//        submitList(currentList)
    }
}