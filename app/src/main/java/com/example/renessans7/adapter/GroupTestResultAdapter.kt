package com.example.renessans7.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.renessans7.databinding.ItemTestResultOfGroupBinding
import com.example.renessans7.models.test.TestCheckResponse

class GroupTestResultAdapter :
    ListAdapter<TestCheckResponse, GroupTestResultAdapter.VH>(DiffUtil()) {

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TestCheckResponse>() {
        override fun areItemsTheSame(
            oldItem: TestCheckResponse,
            newItem: TestCheckResponse
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TestCheckResponse,
            newItem: TestCheckResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    class VH(val binding: ItemTestResultOfGroupBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = VH(
        ItemTestResultOfGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val result = getItem(position)
        holder.binding.apply {
            refreshResultAdapter(result.result, rvResult)
            tvPupilName.text = "${position + 1}. ".plus(result.fullName)
            try {
                tvDate.text = result.resultDate.substring(0, 10).plus("\n")
                    .plus(result.resultDate.substring(10, 16))
            } catch (e: Exception) {
                tvDate.text = result.resultDate
            }
            tvTestResult.text = "${result.unanswered}/${result.wrongAnswers}/${result.trueAnswers}"
        }
    }

    private fun refreshResultAdapter(results: List<Boolean?>, rvResult: RecyclerView) {
        rvResult.isNestedScrollingEnabled = false
        rvResult.adapter = ResultAdapter().apply {
            submitList(results)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}