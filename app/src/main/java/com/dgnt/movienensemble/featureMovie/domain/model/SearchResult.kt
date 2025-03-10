package com.dgnt.movienensemble.featureMovie.domain.model

import com.dgnt.movienensemble.core.util.Constant.DEFAULT_RESULTS_PER_PAGE


data class SearchResult(
    val movies: List<Movie>,
    val totalResults: Int,
    val currentPage: Int,
    val resultsPerPage: Int = DEFAULT_RESULTS_PER_PAGE,
    val errorMessage: String? = null
)
