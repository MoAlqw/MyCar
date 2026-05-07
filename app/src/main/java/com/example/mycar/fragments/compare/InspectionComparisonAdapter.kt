package com.example.mycar.fragments.compare

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.comparison.DamageChangeType
import com.example.mycar.R
import com.example.mycar.databinding.ItemCompareIssueBinding
import com.example.mycar.model.inspection.ItemCompareUi

class InspectionComparisonAdapter: ListAdapter<ItemCompareUi, InspectionComparisonAdapter.ItemCompareViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCompareViewHolder {
        val binding = ItemCompareIssueBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemCompareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemCompareViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemCompareViewHolder(
        private val binding: ItemCompareIssueBinding
    ): RecyclerView.ViewHolder(binding.root) {

        val issueIconRemoved = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.ic_check
        )
        val issueIconAddedOrUnchanged = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.ic_error
        )

        val issueBackgroundRemoved = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.bg_inspection_icon_box
        )
        val issueBackgroundAddedOrUnchanged = AppCompatResources.getDrawable(
            binding.root.context,
            R.drawable.bg_inspection_icon_box_error
        )

        val colorError = binding.root.context.getColor(R.color.error)

        val colorBrandGreen = binding.root.context.getColor(R.color.brand_green)

        fun bind(item: ItemCompareUi) {
            binding.tvIssueTitle.text = item.getNameOfSideAndIssue()
            binding.tvStatusIssue.text = item.type.name

            when (item.type) {
                DamageChangeType.ADDED, DamageChangeType.UNCHANGED -> {
                    binding.ivIssueIcon.setImageDrawable(issueIconAddedOrUnchanged)
                    binding.flIssueIcon.background = issueBackgroundAddedOrUnchanged
                    binding.tvStatusIssue.background = issueBackgroundAddedOrUnchanged
                    binding.tvStatusIssue.setTextColor(colorError)
                }
                DamageChangeType.REMOVED -> {
                    binding.ivIssueIcon.setImageDrawable(issueIconRemoved)
                    ImageViewCompat.setImageTintList(
                        binding.ivIssueIcon,
                        ColorStateList.valueOf(
                            ContextCompat.getColor(binding.root.context, R.color.brand_green)
                        )
                    )
                    binding.ivIssueIcon.backgroundTintList = AppCompatResources.getColorStateList(
                        binding.root.context,
                        R.color.brand_green
                    )
                    binding.tvStatusIssue.background = issueBackgroundRemoved
                    binding.tvStatusIssue.setTextColor(colorBrandGreen)
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ItemCompareUi>() {

            override fun areItemsTheSame(oldItem: ItemCompareUi, newItem: ItemCompareUi): Boolean {
                return oldItem.side == newItem.side
            }

            override fun areContentsTheSame(oldItem: ItemCompareUi, newItem: ItemCompareUi): Boolean {
                return oldItem == newItem
            }
        }
    }
}