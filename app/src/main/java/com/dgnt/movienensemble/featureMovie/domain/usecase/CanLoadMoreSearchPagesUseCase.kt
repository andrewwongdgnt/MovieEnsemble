package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import javax.inject.Inject
import kotlin.math.roundToInt

class CanLoadMoreSearchPagesUseCase @Inject constructor() {

    operator fun invoke(searchResult: SearchResult): Boolean {
        val pages = (searchResult.totalResults.toFloat() / searchResult.resultsPerPage).roundToInt()
        return searchResult.currentPage < pages
    }
}