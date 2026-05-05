package com.example.mycar.fragments.vehicles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Vehicle
import com.example.mycar.databinding.CarCardBinding

class VehiclesAdapter(
    private val onItemClick: (Vehicle) -> Unit
) : ListAdapter<Vehicle, VehiclesAdapter.VehicleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = CarCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VehicleViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class VehicleViewHolder(
        private val binding: CarCardBinding,
        private val onItemClick: (Vehicle) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var vehicle: Vehicle? = null

        init {
            binding.root.setOnClickListener {
                vehicle?.let { onItemClick(it) }
            }
        }

        fun bind(item: Vehicle) {
            vehicle = item

            binding.tvTitle.text = "${item.make} ${item.model}"
            binding.tvPlate.text = item.plate
            binding.tvSubtitle.text = item.year.toString()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Vehicle>() {

            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem == newItem
            }
        }
    }
}