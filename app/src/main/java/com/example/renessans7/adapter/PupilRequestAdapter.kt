package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.databinding.ItemPupilRequestsBinding
import com.example.renessans7.models.requests.RequestsToJoin

class PupilRequestAdapter : ListAdapter<RequestsToJoin, PupilRequestAdapter.VH>(DiffUtil()) {

    lateinit var onClick: ((String) -> Unit)

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<RequestsToJoin>() {
        override fun areItemsTheSame(oldItem: RequestsToJoin, newItem: RequestsToJoin): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RequestsToJoin, newItem: RequestsToJoin): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemPupilRequestsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemPupilRequestsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val request = getItem(position)
        holder.binding.apply {

            tvGroupName.text = request.className
            tvTeacherName.text = request.fullName

            ivCancel.setOnClickListener {
                onClick.invoke(request.requestId)
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