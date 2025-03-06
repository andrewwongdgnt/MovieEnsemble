package com.dgnt.movienensemble.featureMovie.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("Title")
    var title: String,
    @SerializedName("Year")
    var year: String,
    @SerializedName("Poster")
    var poster: String,
    @SerializedName("imdbID")
    var imdbID: String,
    @SerializedName("Type")
    var type: String
)
