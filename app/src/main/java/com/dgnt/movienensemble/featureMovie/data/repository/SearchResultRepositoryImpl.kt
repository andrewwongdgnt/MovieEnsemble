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

        val searchResult = dao.get(searchQuery, page)?.toDomain()
        emit(Resource.Loading(searchResult))


        try {
            val searchResultDto = api.search(searchQuery = searchQuery, page = page, apiKey = apiKey)
            dao.insert(searchResultDto.toData(searchQuery, page))

        } catch (e: HttpException) {
            emit(Resource.Error.HttpError(ex = e, exData = searchResult))
        } catch (e: IOException) {
            emit(Resource.Error.IOError(ex = e, exData = searchResult))
        }

        val newSearchResultEntity = dao.get(searchQuery, page)
        val newSearchResult = newSearchResultEntity?.toDomain()
        newSearchResult?.let {
            emit(Resource.Success(it))
        }
    }
}