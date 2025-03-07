package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.repository.SearchResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchResultRepository
) {
    operator fun invoke(searchQuery: String, page: Int): Flow<Resource<SearchResult>> {
        return searchQuery.takeUnless { it.isBlank() }?.let {
            repository.search(searchQuery, page)
        } ?: flow {}
    }
}