package com.dgnt.movienensemble.featureMovie.presentation

sealed interface MovieListAction {
    data class Search(val searchQuery: String): MovieListAction
    data object LoadMore: MovieListAction
}