package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.R
import com.example.renessans7.databinding.ItemGroupListBinding
import com.example.renessans7.databinding.ItemGroupTestBinding
import com.example.renessans7.databinding.ItemJoinRequestBinding
import com.example.renessans7.databinding.ItemTeacherListBinding
import com.example.renessans7.models.group.Group
import com.example.renessans7.models.requests.RequestsToJoin
import com.example.renessans7.models.teacher.Teacher
import com.example.renessans7.models.test.Test
import com.example.renessans7.utils.Constants.ACCEPT
import com.example.renessans7.utils.Constants.CANCEL
import com.example.renessans7.utils.setStroke
import java.text.SimpleDateFormat

class SentRequestAdapter : ListAdapter<RequestsToJoin, SentRequestAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((String, Int) -> Unit)

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<RequestsToJoin>() {
        override fun areItemsTheSame(oldItem: RequestsToJoin, newItem: RequestsToJoin): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RequestsToJoin, newItem: RequestsToJoin): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemJoinRequestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemJoinRequestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val request = getItem(position)
        holder.binding.apply {

            tvGroupName.text = request.className
            tvPupilName.text = request.fullName

            ivDone.setOnClickListener {
                onClick.invoke(request.requestId, ACCEPT)
            }
            ivCancel.setOnClickListener {
                onClick.invoke(request.requestId, CANCEL)
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