package com.dgnt.movienensemble.featureMovie.domain.usecase

import com.dgnt.movienensemble.featureMovie.domain.repository.SearchResultRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repository: SearchResultRepository
) {
    operator fun invoke(searchQuery: String, page: Int)=
        repository.search(searchQuery, page)

}