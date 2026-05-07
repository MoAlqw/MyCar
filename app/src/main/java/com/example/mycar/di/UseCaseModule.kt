package com.example.mycar.di

import com.example.domain.comparator.InspectionComparator
import com.example.domain.repository.baseline.BaselineVehicleRepository
import com.example.domain.repository.inspection.InspectionRepository
import com.example.domain.repository.vehicle.VehicleRepository
import com.example.domain.usecase.AddVehicleUseCase
import com.example.domain.usecase.CompareInspectionUseCase
import com.example.domain.usecase.DeleteVehicleUseCase
import com.example.domain.usecase.GetAllInspectionsOfVehicleUseCase
import com.example.domain.usecase.GetBaselineUseCase
import com.example.domain.usecase.GetInspectionUseCase
import com.example.domain.usecase.GetVehicleDetailsUseCase
import com.example.domain.usecase.ObserveVehiclesUseCase
import com.example.domain.usecase.SetBaselineUseCase
import com.example.domain.usecase.SyncInspectionsUseCase
import com.example.domain.usecase.UploadInspectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddVehicleUseCase(
        vehicleRepository: VehicleRepository
    ): AddVehicleUseCase {
        return AddVehicleUseCase(vehicleRepository)
    }

    @Provides
    fun provideObserveVehiclesUseCase(
        vehicleRepository: VehicleRepository
    ): ObserveVehiclesUseCase {
        return ObserveVehiclesUseCase(vehicleRepository)
    }

    @Provides
    fun provideDeleteVehicleUseCase(
        vehicleRepository: VehicleRepository
    ): DeleteVehicleUseCase {
        return DeleteVehicleUseCase(vehicleRepository)
    }

    @Provides
    fun provideGetVehicleDetailsUseCase(
        vehicleRepository: VehicleRepository
    ): GetVehicleDetailsUseCase {
        return GetVehicleDetailsUseCase(vehicleRepository)
    }

    @Provides
    fun provideUploadInspectionUseCase(
        inspectionRepository: InspectionRepository
    ): UploadInspectionUseCase {
        return UploadInspectionUseCase(inspectionRepository)
    }

    @Provides
    fun provideGetInspectionUseCase(
        inspectionRepository: InspectionRepository
    ): GetInspectionUseCase {
        return GetInspectionUseCase(inspectionRepository)
    }

    @Provides
    fun provideGetAllInspectionsOfVehicleUseCase(
        inspectionRepository: InspectionRepository
    ): GetAllInspectionsOfVehicleUseCase {
        return GetAllInspectionsOfVehicleUseCase(inspectionRepository)
    }

    @Provides
    fun provideSyncInspectionsUseCase(
        inspectionRepository: InspectionRepository
    ): SyncInspectionsUseCase {
        return SyncInspectionsUseCase(inspectionRepository)
    }

    @Provides
    fun provideGetBaselineUseCase(
        baselineVehicleRepository: BaselineVehicleRepository,
        inspectionRepository: InspectionRepository
    ): GetBaselineUseCase {
        return GetBaselineUseCase(baselineVehicleRepository, inspectionRepository)
    }

    @Provides
    fun provideSetBaselineUseCase(
        baselineVehicleRepository: BaselineVehicleRepository
    ): SetBaselineUseCase {
        return SetBaselineUseCase(baselineVehicleRepository)
    }

    @Provides
    fun provideCompareInspectionUseCase(
        inspectionRepository: InspectionRepository,
        comparator: InspectionComparator
    ): CompareInspectionUseCase {
        return CompareInspectionUseCase(inspectionRepository, comparator)
    }
}