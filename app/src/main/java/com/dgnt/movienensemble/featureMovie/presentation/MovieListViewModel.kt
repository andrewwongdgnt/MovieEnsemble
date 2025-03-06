package com.dgnt.movienensemble.featureMovie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(

    private val searchUseCase: SearchUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY_PROCESSING_DELAY_MILLI = 500L
    }

    private val _state: MutableStateFlow<MovieListState> = MutableStateFlow(MovieListState.Empty(""))
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    private var searchQueryJob: Job? = null

    fun onAction(action: MovieListAction) {
        when (action) {
            is MovieListAction.Search -> onSearch(action.searchQuery)
            MovieListAction.LoadMore -> onLoadMore()
        }
    }

    private fun onSearch(
        searchQuery: String,
        page: Int = 1,
        searchProcessingDelay: Long = SEARCH_QUERY_PROCESSING_DELAY_MILLI
    ) {
        val initialLoad = page == 1
        _state.value = state.value.new(searchQuery)
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            delay(searchProcessingDelay)
            searchUseCase(searchQuery, page)
                .onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.d("TAG", "Error")
                        }

                        is Resource.Loading -> {
                            if (initialLoad)
                                _state.value = MovieListState.Loading(state.value.searchQuery)
                        }

                        is Resource.Success -> {
                            val searchResult = result.data ?: SearchResult(emptyList(), 0, page)
                            val finalSearchResult = if (initialLoad) {
                                searchResult
                            } else {
                                val newMovieList = searchResult.movies
                                val currentMovieList = ((state.value as? MovieListState.Result)?.searchResult?.movies ?: emptyList())
                                SearchResult(currentMovieList + newMovieList, 1, page)
                            }
                            _state.value = MovieListState.Result(
                                sq = state.value.searchQuery,
                                searchResult = finalSearchResult
                            )
                        }
                    }

                }.launchIn(this)
        }
    }

    private fun onLoadMore() {
        (state.value as? MovieListState.Result)?.let {
            val newPage = it.searchResult.currentPage + 1
            onSearch(it.searchQuery, newPage, 100L)
        }
    }
}