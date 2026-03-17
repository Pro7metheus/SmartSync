package com.smartpantry.data.api

import com.smartpantry.data.model.usda.UsdaSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UsdaApi {
    @GET("foods/search")
    suspend fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("dataType") dataType: String = "Branded,Foundation,SR Legacy"
    ): Response<UsdaSearchResponse>
}
