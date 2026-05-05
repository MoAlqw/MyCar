package com.example.mycar.fragments.inspection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.R
import com.example.mycar.databinding.ItemDetectedIssueBinding
import com.example.mycar.model.inspection.IssuesDetectionsUi

class InspectionOfVehicleAdapter:
    ListAdapter<IssuesDetectionsUi, InspectionOfVehicleAdapter.IssuesOfVehicleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssuesOfVehicleViewHolder {
        val binding = ItemDetectedIssueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IssuesOfVehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssuesOfVehicleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class IssuesOfVehicleViewHolder(
        private val binding: ItemDetectedIssueBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val mediumConfidence = binding.root.context.getString(R.string.medium_confidence)
        val highConfidence = binding.root.context.getString(R.string.high_confidence)

        fun bind(item: IssuesDetectionsUi) {
            binding.tvIssueTitle.text = item.name
            binding.tvSeverity.text = item.levelOfSeverity
            binding.tvConfidenceBadge.text =
               if (item.confidence < 75) mediumConfidence else highConfidence
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<IssuesDetectionsUi>() {

            override fun areItemsTheSame(oldItem: IssuesDetectionsUi, newItem: IssuesDetectionsUi): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: IssuesDetectionsUi, newItem: IssuesDetectionsUi): Boolean {
                return oldItem == newItem
            }
        }
    }
}