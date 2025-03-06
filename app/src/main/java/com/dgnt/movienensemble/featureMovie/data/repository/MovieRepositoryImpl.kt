package com.dgnt.movienensemble.featureMovie.data.repository

import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.data.remote.MovieApi
import com.dgnt.movienensemble.featureMovie.data.remote.dto.MovieDto
import com.dgnt.movienensemble.featureMovie.data.remote.dto.SearchResultDto
import com.dgnt.movienensemble.featureMovie.domain.model.Movie
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val api: MovieApi
) : MovieRepository {
    override fun search(searchQuery: String, page: Int, apiKey: String): Flow<Resource<SearchResult>> = flow {
        emit(Resource.Loading())

        try {
            val searchResult = api.search(searchQuery = searchQuery, page = page, apiKey = apiKey)
            emit(Resource.Success(searchResult.toDomain(page)))
        } catch (e: HttpException) {
            emit(Resource.Error())
        } catch (e: IOException) {
            emit(Resource.Error())
        }
    }

    //TODO create custom mappers
    private fun SearchResultDto.toDomain(page:Int) =
        SearchResult(
            movies = results?.map { it.toDomain() } ?: emptyList(),
            totalResults = totalResults?.toIntOrNull() ?: 0,
            currentPage = page
        )

    private fun MovieDto.toDomain() =
        Movie(
            title = title,
            year = year,
            poster = poster,
            imdbID = imdbID,
            type = type,
        )
}