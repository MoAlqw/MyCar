package com.example.mycar.fragments.vehicledetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mycar.R
import com.example.mycar.databinding.ItemInspectionCardBinding
import com.example.mycar.model.inspection.InspectionUi

class VehicleInspectionsAdapter(
    private val onItemClick: (String) -> Unit
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

    class VehicleInspectionsViewHolder(
        private val binding: ItemInspectionCardBinding,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var inspectionId: String = ""

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
        private val tintError = ContextCompat.getColorStateList(
            binding.root.context,
            R.color.error_light
        )

        init {
            binding.root.setOnClickListener {
                onItemClick(inspectionId)
            }
        }

        fun bind(item: InspectionUi) {
            inspectionId = item.id
            binding.tvInspectionSubtitle.text = item.displayDate

            if (item.damageStatus > 0) {
                binding.flInspectionIcon.background = backgroundError
                binding.ivInspectionOfVehicle.setImageDrawable(iconError)
                binding.ivInspectionOfVehicle.imageTintList = tintError
            } else {
                binding.flInspectionIcon.background = backgroundSuccess
                binding.ivInspectionOfVehicle.setImageDrawable(iconSuccess)
                binding.ivInspectionOfVehicle.imageTintList = tintSuccess
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