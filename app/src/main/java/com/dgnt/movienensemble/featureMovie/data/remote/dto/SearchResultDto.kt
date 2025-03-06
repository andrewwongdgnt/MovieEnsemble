package com.dgnt.movienensemble.featureMovie.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchResultDto(
    @SerializedName("Search")
    val results: List<MovieDto>,
    val totalResults: String,
)
