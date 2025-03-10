package com.dgnt.movienensemble.featureMovie.presentation

import androidx.annotation.StringRes
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult


sealed class MovieListState(val searchQuery: String) {

    abstract fun new(searchQuery: String): MovieListState
    data class Empty(
        private val sq: String = "",
        @StringRes val messageRes: Int = R.string.emptyResultMsg
    ) : MovieListState(sq) {
        override fun new(searchQuery: String) = copy(sq = searchQuery)
    }

    data class Loading(private val sq: String) : MovieListState(sq) {
        override fun new(searchQuery: String) = copy(sq = searchQuery)
    }

    data class Result(
        val sq: String,
        val searchResult: SearchResult,
        val isLoadingMore: Boolean = false,
    ) : MovieListState(sq) {
        override fun new(searchQuery: String) = copy(sq = searchQuery)
    }
}