package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import coil.compose.AsyncImage
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.presentation.composable.EndlessLazyColumn
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
        onAction = viewModel::onAction
    )
}

@Composable
private fun MovieListContentInner(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(value = state.searchQuery, onValueChange = {
            onAction(MovieListAction.Search(it))
        }, modifier = Modifier.fillMaxWidth(), placeholder = {
            Text(text = stringResource(R.string.searchMovie))
        })
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is MovieListState.Empty -> EmptyResult()
            is MovieListState.Loading -> LoadingResults()
            is MovieListState.Result -> MovieResults(
                searchResult = state.searchResult,
                loadMore = {
                    onAction(MovieListAction.LoadMore)
                }
            )
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
            text = stringResource(R.string.emptyResultMsg), modifier = modifier.align(Alignment.Center)
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
    searchResult: SearchResult,
    loadMore: () -> Unit
) {
    EndlessLazyColumn(
        modifier = modifier.fillMaxSize(),
        items = searchResult.movies,
        loadMoreItems = loadMore,
        content = { movie ->
            ListItem(
                modifier = modifier.padding(bottom = 5.dp),
                leadingContent = {
                    AsyncImage(
                        model = movie.poster,
                        contentDescription = null,
                    )
                },
                overlineContent = { Text(text = movie.year) },
                headlineContent = { Text(text = movie.title) },
                supportingContent = {
                    Button(onClick = {}) {
                        Text(text = stringResource(R.string.movieDetail))
                    }
                },
                tonalElevation = 5.dp,
                shadowElevation = 5.dp,
            )
        }

    )
}

@Previews
@Composable
private fun MovieListContentPreview(
    @PreviewParameter(MovieListPreviewParameterProvider::class) state: MovieListPreviewState
) = MovieEnsembleTheme {
    Surface {
        MovieListContentInner(
            state = state.state,
            onAction = {}
        )
    }
}