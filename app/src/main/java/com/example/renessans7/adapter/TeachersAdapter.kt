package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.utils.setStroke

class TeachersAdapter : ListAdapter<Teacher, TeachersAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((Teacher) -> Unit)
    private var lastSelectedPos = -1
    private var currentSelectedPos = -1

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Teacher>() {
        override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemTeacherListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemTeacherListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {

            tvTeacherName.text = getItem(position).fullName

            if (currentSelectedPos == holder.adapterPosition)
                root.setStroke(3, root.context.resources.getColor(R.color.main_red))
            else
                root.strokeWidth = 0

            root.setOnClickListener {
                lastSelectedPos = currentSelectedPos
                currentSelectedPos = holder.adapterPosition
                root.setStroke(3, root.context.resources.getColor(R.color.main_red))
                onClick.invoke(getItem(position))
                if (lastSelectedPos != -1) {
                    notifyItemChanged(lastSelectedPos)
                }
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