package com.dgnt.movienensemble.featureMovie.data.repository

import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.data.local.dao.SearchResultDao
import com.dgnt.movienensemble.featureMovie.data.remote.OMDBApi
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.repository.SearchResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchResultRepositoryImpl(
    private val api: OMDBApi,
    private val dao: SearchResultDao
) : SearchResultRepository {
    override fun search(searchQuery: String, page: Int, apiKey: String): Flow<Resource<SearchResult>> = flow {
        emit(Resource.Loading())

        try {
            val searchResult = api.search(searchQuery = searchQuery, page = page, apiKey = apiKey)
            emit(Resource.Success(searchResult.toDomain(page)))
        } catch (e: HttpException) {
            emit(Resource.Error.HttpError(e = e))
        } catch (e: IOException) {
            emit(Resource.Error.IOError(e = e))
        }
    }
}