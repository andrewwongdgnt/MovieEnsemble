package com.dgnt.movienensemble.featureMovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult

@Entity
data class SearchResultEntity(
    @PrimaryKey val id: Int? = null,
    val searchQuery: String,
    val movies: List<MovieData>,
    val totalResults: Int,
    val currentPage: Int,
    val errorMessage: String?

) {
    fun toDomain() =
        SearchResult(
            movies = movies.map { it.toDomain() },
            totalResults = totalResults,
            currentPage = currentPage,
            errorMessage = errorMessage
        )
}