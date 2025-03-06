package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

data class MovieListPreviewState(
    val searchQuery: String
)

class MovieListPreviewParameterProvider : CollectionPreviewParameterProvider<MovieListPreviewState> (
    listOf(
        MovieListPreviewState(
            searchQuery = ""
        ),
        MovieListPreviewState(
            searchQuery = "Star Wars"
        ),
    )
)