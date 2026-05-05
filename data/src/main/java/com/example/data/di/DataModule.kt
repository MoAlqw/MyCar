package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.db.AppDatabase
import com.example.data.db.dao.InspectionDao
import com.example.data.db.dao.VehicleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mycar.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideVehicleDao(
        database: AppDatabase
    ): VehicleDao {
        return database.vehicleDao()
    }

    @Provides
    fun provideInspectionDao(
        database: AppDatabase
    ): InspectionDao {
        return database.inspectionDao()
    }
}