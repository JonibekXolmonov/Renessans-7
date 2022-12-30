package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.databinding.ItemPupilBinding
import com.example.renessans7.models.pupils.Pupil

class GroupPupilAdapter : ListAdapter<Pupil, GroupPupilAdapter.VH>(DiffUtil()) {

    lateinit var onRemove: (String) -> Unit

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Pupil>() {
        override fun areItemsTheSame(oldItem: Pupil, newItem: Pupil): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Pupil, newItem: Pupil): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemPupilBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemPupilBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val pupil = getItem(position)
        holder.binding.apply {
            tvPupilName.text = pupil.fullName

            ivCancel.setOnClickListener {
                onRemove(pupil.pupilId)
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