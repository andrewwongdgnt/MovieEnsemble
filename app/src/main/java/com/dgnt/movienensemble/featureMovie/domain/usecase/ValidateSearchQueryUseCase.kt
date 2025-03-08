package com.dgnt.movienensemble.featureMovie.domain.usecase

import javax.inject.Inject

class ValidateSearchQueryUseCase @Inject constructor() {
    operator fun invoke(searchQuery: String) =
        searchQuery.isNotBlank()
}