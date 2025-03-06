package com.dgnt.movienensemble.featureMovie.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(

) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY_PROCESSING_DELAY_MILLI = 500L
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchQueryJob: Job? = null

    fun onSearch(searchQuery: String) {
        _searchQuery.value = searchQuery
        searchQueryJob?.cancel()
        searchQueryJob = viewModelScope.launch {
            delay(SEARCH_QUERY_PROCESSING_DELAY_MILLI)
        }
    }
}