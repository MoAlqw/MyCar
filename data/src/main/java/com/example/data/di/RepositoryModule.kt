package com.example.data.di

import com.example.data.comparator.InspectionComparatorImpl
import com.example.data.repository.BaselineVehicleRepositoryImpl
import com.example.data.repository.InspectionRepositoryImpl
import com.example.data.repository.VehicleRepositoryImpl
import com.example.domain.comparator.InspectionComparator
import com.example.domain.repository.baseline.BaselineVehicleRepository
import com.example.domain.repository.inspection.InspectionRepository
import com.example.domain.repository.vehicle.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindVehicleRepository(
        impl: VehicleRepositoryImpl
    ): VehicleRepository

    @Binds
    abstract fun bindInspectionRepository(
        impl: InspectionRepositoryImpl
    ): InspectionRepository

    @Binds
    abstract fun bindBaselineVehicleRepository(
        impl: BaselineVehicleRepositoryImpl
    ): BaselineVehicleRepository

    @Binds
    abstract fun bindInspectionComparator(
        impl: InspectionComparatorImpl
    ): InspectionComparator
}