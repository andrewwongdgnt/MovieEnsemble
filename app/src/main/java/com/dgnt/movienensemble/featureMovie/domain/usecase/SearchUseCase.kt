package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.data.remote.dto.SearchResultDto
import com.dgnt.movienensemble.featureMovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(searchQuery: String, page: Int = 1): Flow<Resource<SearchResultDto>> {
        return searchQuery.takeUnless { it.isBlank() }?.let {
            repository.search(searchQuery, page)
        } ?: flow {}
    }
}