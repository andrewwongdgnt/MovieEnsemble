package com.dgnt.movienensemble.featureMovie.data.local.entity

import com.dgnt.movienensemble.featureMovie.domain.model.Movie

data class MovieData(
    var title: String,
    var year: String,
    var poster: String,
    var imdbID: String,
    var type: String
) {
    fun toDomain() =
        Movie(
            title = title,
            year = year,
            poster = poster,
            imdbID = imdbID,
            type = type
        )
}
