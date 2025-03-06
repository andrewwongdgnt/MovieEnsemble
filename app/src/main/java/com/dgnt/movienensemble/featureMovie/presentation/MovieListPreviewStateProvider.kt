package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.dgnt.movienensemble.featureMovie.domain.model.Movie
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult

data class MovieListPreviewState(
    val state: MovieListState
)

class MovieListPreviewParameterProvider : CollectionPreviewParameterProvider<MovieListPreviewState>(
    listOf(
        MovieListPreviewState(
            state = MovieListState.Empty(sq = "")
        ),
        MovieListPreviewState(
            state = MovieListState.Loading(sq = "")
        ),
        MovieListPreviewState(
            state = MovieListState.Loading(sq = "Big Name")
        ),
        MovieListPreviewState(
            state = MovieListState.Result(
                sq = "Star Wars",
                searchResult = SearchResult(
                    listOf(
                        Movie(
                            title = "Star Wars",
                            year = "2004",
                            poster = "",
                            imdbID = "123",
                            type = "Movie"
                        ),
                        Movie(
                            title = "Star Wars 2",
                            year = "2005",
                            poster = "",
                            imdbID = "123",
                            type = "Movie"
                        ),
                        Movie(
                            title = "Super Star Wars",
                            year = "2005",
                            poster = "",
                            imdbID = "123",
                            type = "Movie"
                        ),
                        Movie(
                            title = "Cool Star Wars",
                            year = "1988",
                            poster = "",
                            imdbID = "123",
                            type = "Movie"
                        )
                    )
                )
            )
        ),
    )
)