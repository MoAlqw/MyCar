package com.example.mycar.fragments.compare

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mycar.fragments.inspection.fragment.InspectionOfVehicleFragment
import com.example.mycar.model.type.PageType

class InspectionPagerAdapter(
    fragment: Fragment,
    private val pages: List<PageType>,
    private val inspectionId: String,
    private val baselineId: String?
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int): Fragment {
        return when (pages[position]) {
            PageType.ANALYSIS -> InspectionOfVehicleFragment.newInstance(inspectionId)
            PageType.COMPARISON -> InspectionComparisonFragment.newInstance(inspectionId, baselineId!!)
        }
    }
}