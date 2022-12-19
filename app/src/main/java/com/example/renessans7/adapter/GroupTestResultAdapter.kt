package com.example.renessans7.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.renessans7.databinding.ItemGroupTestNumberBinding
import com.example.renessans7.databinding.ItemTestResultOfGroupBinding
import com.example.renessans7.models.TestResult
import com.example.renessans7.utils.Constants.NORMAL_TYPE
import com.example.renessans7.utils.Constants.NUMBER_TYPE

class GroupTestResultAdapter : ListAdapter<TestResult, ViewHolder>(DiffUtil()) {

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TestResult>() {
        override fun areItemsTheSame(oldItem: TestResult, newItem: TestResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TestResult, newItem: TestResult): Boolean {
            return oldItem == newItem
        }
    }

    class VHNormal(val binding: ItemTestResultOfGroupBinding) : ViewHolder(binding.root)

    class VHNumber(val binding: ItemGroupTestNumberBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        return if (viewType == NORMAL_TYPE) VHNormal(
            ItemTestResultOfGroupBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        else VHNumber(
            ItemGroupTestNumberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is VHNumber -> {
                holder.binding.apply {
                    refreshTestNumberAdapter(rvTestNumber, 35)
                }
            }
            is VHNormal -> {
                holder.binding.apply {

                    tvPupilName.text = getItem(position).name
                    tvTestResult.text = getItem(position).inc.plus("/").plus(getItem(position).cor)

                    refreshResultAdapter(getItem(position).results, rvResult)
                }
            }
        }
    }

    private fun refreshTestNumberAdapter(rvTestNumber: RecyclerView, listSize: Int) {
        rvTestNumber.isNestedScrollingEnabled = false
        rvTestNumber.adapter = TestNumberAdapter().apply {
            submitList(ArrayList<Int>().apply {
                for (i in 0 until listSize) this.add(i + 1)
            })
        }
    }

    private fun refreshResultAdapter(results: List<Boolean>, rvResult: RecyclerView) {
        rvResult.isNestedScrollingEnabled = false
        rvResult.adapter = ResultAdapter().apply {
            submitList(results)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) NUMBER_TYPE else NORMAL_TYPE
    }
}