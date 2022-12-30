package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.renessans7.databinding.ItemTestAnswerBinding
import com.example.renessans7.models.test.TestAnswers
import com.example.renessans7.utils.Constants.A
import com.example.renessans7.utils.Constants.B
import com.example.renessans7.utils.Constants.C
import com.example.renessans7.utils.Constants.D

class AnswerInsertionAdapter : ListAdapter<TestAnswers, AnswerInsertionAdapter.VH>(DiffUtil()) {

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TestAnswers>() {
        override fun areItemsTheSame(oldItem: TestAnswers, newItem: TestAnswers): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TestAnswers, newItem: TestAnswers): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemTestAnswerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            ItemTestAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {

            tvTestNumber.text = "${position + 1}"

            rbOptionA.setOnClickListener {
                saveAnswer(position, A)
            }
            rbOptionB.setOnClickListener {
                saveAnswer(position, B)
            }
            rbOptionC.setOnClickListener {
                saveAnswer(position, C)
            }
            rbOptionD.setOnClickListener {
                saveAnswer(position, D)
            }
        }
    }

    private fun saveAnswer(position: Int, answer: String) {
        currentList[position].testAnswer = answer
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}