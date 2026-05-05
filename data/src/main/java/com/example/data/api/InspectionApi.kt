package com.example.data.api

import com.example.data.api.model.InspectionResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface InspectionApi {

    @Multipart
    @POST("/api/v1/inspections/analyze")
    suspend fun analyzeInspection(
        @Part("vehicle_id") vehicleId: RequestBody,
        @Part frontPhoto: MultipartBody.Part,
        @Part rearPhoto: MultipartBody.Part,
        @Part leftPhoto: MultipartBody.Part,
        @Part rightPhoto: MultipartBody.Part
    ): Response<InspectionResponseDto>

    @GET("/api/v1/vehicles/{vehicle_id}/inspections/full")
    suspend fun getVehicleInspections(
        @Path("vehicle_id") vehicleId: String
    ): Response<List<InspectionResponseDto>>
}