package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.presentation.preview.Previews
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.ui.theme.MovieEnsembleTheme

@Composable
fun MovieListContent(
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MovieListContentInner(
        state = state,
        onSearch = viewModel::onSearch
    )
}

@Composable
private fun MovieListContentInner(
    state: MovieListState,
    onSearch: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = state.searchQuery,
            onValueChange = onSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = stringResource(R.string.searchMovie))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is MovieListState.Empty -> EmptyResult()
            is MovieListState.Loading -> LoadingResults()
            is MovieListState.Result -> MovieResults(searchResult = state.searchResult)
        }


    }
}

@Composable
private fun EmptyResult(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.emptyResultMsg),
            modifier = modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun LoadingResults(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .size(72.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun MovieResults(
    modifier: Modifier = Modifier,
    searchResult: SearchResult
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(searchResult.movies) { movie ->
            ListItem(
                modifier = modifier.padding(bottom = 5.dp),
                headlineContent = { Text(text = movie.title) },
                supportingContent = { Text(text = movie.year) },
                tonalElevation = 5.dp,
                shadowElevation = 5.dp,
            )
        }
    }
}

@Previews
@Composable
private fun MovieListContentPreview(
    @PreviewParameter(MovieListPreviewParameterProvider::class) state: MovieListPreviewState
) = MovieEnsembleTheme {
    Surface {
        MovieListContentInner(
            state = state.state,
            onSearch = {}
        )
    }
}