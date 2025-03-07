package com.dgnt.movienensemble.featureMovie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.presentation.uievent.UiEvent
import com.dgnt.movienensemble.core.util.Resource
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.featureMovie.domain.usecase.CanLoadMoreSearchPagesUseCase
import com.dgnt.movienensemble.featureMovie.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val canLoadMoreSearchPagesUseCase: CanLoadMoreSearchPagesUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY_PROCESSING_DELAY_MILLI = 500L
        private const val TAG = "MovieListVM"
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state: MutableStateFlow<MovieListState> = MutableStateFlow(MovieListState.Empty())
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
                            Log.e(TAG, "Could not load search result", result.exception)
                                                        
                            val message = when (result) {
                                is Resource.Error.HttpError -> R.string.serverError
                                is Resource.Error.IOError -> R.string.genericError
                            }
                            if (initialLoad) {
                                if (result.data == null)
                                    _state.value = MovieListState.Empty(searchQuery, message)
                            } else {
                                if (result.data == null) {
                                    _uiEvent.send(UiEvent.SnackBar(message = message))
                                }
                                (state.value as? MovieListState.Result)?.let {
                                    _state.value = it.copy(isLoadingMore = false)
                                }
                            }
                        }

                        is Resource.Loading -> {
                            if (initialLoad) {
                                _state.value = result.data?.let {
                                    MovieListState.Result(
                                        sq = searchQuery,
                                        searchResult = it
                                    )
                                } ?: MovieListState.Loading(searchQuery)
                            } else
                                (state.value as? MovieListState.Result)?.let {
                                    _state.value = it.copy(isLoadingMore = true)
                                }
                        }

                        is Resource.Success -> {
                            val searchResult = result.data ?: SearchResult(emptyList(), 0, page)
                            val finalSearchResult = if (initialLoad) {
                                searchResult
                            } else {
                                val newMovieList = searchResult.movies
                                val currentMovieList = ((state.value as? MovieListState.Result)?.searchResult?.movies ?: emptyList())
                                SearchResult(currentMovieList + newMovieList, searchResult.totalResults, page)
                            }
                            _state.value = MovieListState.Result(
                                sq = searchQuery,
                                searchResult = finalSearchResult
                            )
                        }
                    }

                }.launchIn(this)
        }
    }

    private fun onLoadMore() {
        (state.value as? MovieListState.Result)
            ?.takeIf { canLoadMoreSearchPagesUseCase(it.searchResult) }
            ?.let {
                val newPage = it.searchResult.currentPage + 1
                onSearch(it.searchQuery, newPage, 0)
            }
    }
}