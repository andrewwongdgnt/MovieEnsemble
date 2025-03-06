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

    fun onSearch(searchQuery: String) {
        _state.value = state.value.new(searchQuery)
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            delay(SEARCH_QUERY_PROCESSING_DELAY_MILLI)
            searchUseCase(searchQuery)
                .onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.d("TAG", "Error")
                        }

                        is Resource.Loading -> {
                            _state.value = MovieListState.Loading(state.value.searchQuery)
                        }

                        is Resource.Success -> {
                            _state.value = MovieListState.Result(
                                sq = state.value.searchQuery,
                                searchResult = result.data ?: SearchResult(emptyList())
                            )
                        }
                    }

                }.launchIn(this)
        }
    }
}