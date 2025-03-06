package com.dgnt.movienensemble.featureMovie.domain.repository

import com.dgnt.movienensemble.core.util.Constant.DEFAULT_OMBD_API_KEY
import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.data.remote.dto.SearchResultDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun search(searchQuery: String, page: Int = 1, apiKey: String = DEFAULT_OMBD_API_KEY): Flow<Resource<SearchResultDto>>
}