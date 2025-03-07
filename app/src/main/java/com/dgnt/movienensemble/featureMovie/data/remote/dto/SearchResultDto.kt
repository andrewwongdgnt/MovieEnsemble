package com.dgnt.movienensemble.featureMovie.data.remote.dto

import com.dgnt.movienensemble.featureMovie.data.local.entity.SearchResultEntity
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.google.gson.annotations.SerializedName

data class SearchResultDto(
    @SerializedName("Search")
    val results: List<MovieDto>?,

    val totalResults: String?,

    @SerializedName("Error")
    val error: String?
) {

    fun toDomain(page: Int) =
        SearchResult(
            movies = results?.map { it.toDomain() } ?: emptyList(),
            totalResults = totalResults?.toIntOrNull() ?: 0,
            currentPage = page
        )

    fun toData(searchQuery: String, page: Int) =
        SearchResultEntity(
            searchQuery = searchQuery,
            movies = results?.map { it.toData() } ?: emptyList(),
            totalResults = totalResults?.toIntOrNull() ?: 0,
            currentPage = page,
            errorMessage = error
        )

}
