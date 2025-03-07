package com.dgnt.movienensemble.featureMovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchResultEntity (
    @PrimaryKey val id: Int? = null,
    val searchQuery: String,
    val movies: List<MovieData>,
    val totalResults: Int,
    val currentPage: Int

)