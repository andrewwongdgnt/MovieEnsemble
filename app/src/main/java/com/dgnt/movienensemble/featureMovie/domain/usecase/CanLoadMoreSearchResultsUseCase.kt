package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import javax.inject.Inject
import kotlin.math.ceil

class CanLoadMoreSearchResultsUseCase @Inject constructor() {

    operator fun invoke(searchResult: SearchResult): Boolean {
        val pages = ceil(searchResult.totalResults.toFloat() / searchResult.resultsPerPage).toInt()
        return searchResult.currentPage < pages
    }
}