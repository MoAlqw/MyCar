package com.example.data.di

import com.example.data.repository.InspectionRepositoryImpl
import com.example.data.repository.VehicleRepositoryImpl
import com.example.domain.repository.InspectionRepository
import com.example.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVehicleRepository(
        impl: VehicleRepositoryImpl
    ): VehicleRepository

    @Binds
    abstract fun bindInspectionRepository(
        impl: InspectionRepositoryImpl
    ): InspectionRepository
}