package com.example.mycar.fragments.vehicledetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.R
import com.example.mycar.databinding.ItemInspectionCardBinding
import com.example.mycar.model.inspection.InspectionUi

class VehicleInspectionsAdapter(
    private val onItemClick: (String) -> Unit,
    private val onLongClick: (InspectionUi, View) -> Unit
): ListAdapter<InspectionUi, VehicleInspectionsAdapter.VehicleInspectionsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleInspectionsViewHolder {
        val binding = ItemInspectionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VehicleInspectionsViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: VehicleInspectionsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VehicleInspectionsViewHolder(
        private val binding: ItemInspectionCardBinding,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val backgroundSuccess = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.bg_inspection_icon_box
        )
        private val backgroundError = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.bg_inspection_icon_box_error
        )
        private val iconSuccess = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.ic_clipboard_check
        )
        private val iconError = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.ic_error
        )
        private val tintSuccess = ContextCompat.getColorStateList(
            binding.root.context,
            R.color.brand_green
        )

        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(adapterPosition).id)
            }
            binding.root.setOnLongClickListener {
                onLongClick(getItem(adapterPosition), it)
                true
            }
        }

        fun bind(item: InspectionUi) {
            binding.tvInspectionSubtitle.text = item.displayDate

            if (item.isBaseline) {
                binding.mcwInspection.strokeWidth = 6
                binding.mcwInspection.strokeColor = ContextCompat.getColor(
                    binding.root.context,
                    R.color.gold
                )
                binding.mcwInspection.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.darkness_green)
                )
            }

            if (item.damageStatus > 0) {
                binding.flInspectionIcon.background = backgroundError
                binding.ivInspectionOfVehicle.setImageDrawable(iconError)
            } else {
                binding.flInspectionIcon.background = backgroundSuccess
                binding.ivInspectionOfVehicle.setImageDrawable(iconSuccess)
                binding.ivInspectionOfVehicle.imageTintList = tintSuccess
            }

            if (item.addedIssues > 0) {
                binding.tvAddedIssues.visibility = View.VISIBLE
                binding.tvAddedIssues.text =
                    binding.root.context.getString(R.string.issues_added, item.addedIssues)
            } else {
                binding.tvAddedIssues.visibility = View.GONE
            }

            if (item.removedIssues > 0) {
                binding.tvRemovedIssues.visibility = View.VISIBLE
                binding.tvRemovedIssues.text =
                    binding.root.context.getString(R.string.issues_removed, item.removedIssues)
            } else {
                binding.tvRemovedIssues.visibility = View.GONE
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<InspectionUi>() {

            override fun areItemsTheSame(oldItem: InspectionUi, newItem: InspectionUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: InspectionUi, newItem: InspectionUi): Boolean {
                return oldItem == newItem
            }
        }
    }
}