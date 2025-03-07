package com.dgnt.movienensemble.featureMovie.data.remote

import com.dgnt.movienensemble.featureMovie.data.remote.dto.SearchResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBApi {
    companion object {
        const val BASE_URL = "https://www.omdbapi.com/"
    }
    @GET("/")
    suspend fun search(
        @Query("s") searchQuery: String,
        @Query("page") page: Int,
        @Query("type") type: String = "movie",
        @Query("apikey") apiKey: String,
    ): SearchResultDto
}