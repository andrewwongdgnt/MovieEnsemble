package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.presentation.composable.DefaultSnackbar
import com.dgnt.movienensemble.core.presentation.composable.EndlessLazyColumn
import com.dgnt.movienensemble.core.presentation.preview.Previews
import com.dgnt.movienensemble.core.presentation.uievent.UiEvent
import com.dgnt.movienensemble.featureMovie.domain.model.SearchResult
import com.dgnt.movienensemble.ui.theme.MovieEnsembleTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun MovieListContent(
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MovieListContentInner(
        uiEvent = viewModel.uiEvent,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MovieListContentInner(
    uiEvent: Flow<UiEvent>,
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.SnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = context.resources.getString(event.message)
                    )
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState) { snackbarData ->
                DefaultSnackbar(snackbarData = snackbarData)
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            TextField(value = state.searchQuery, onValueChange = {
                onAction(MovieListAction.Search(it))
            }, modifier = Modifier.fillMaxWidth(), placeholder = {
                Text(text = stringResource(R.string.searchMovie))
            })
            Spacer(modifier = Modifier.height(16.dp))
            when (state) {
                is MovieListState.Empty -> EmptyResult(message = stringResource(state.messageRes))
                is MovieListState.Loading -> LoadingResults()
                is MovieListState.Result -> MovieResults(
                    searchResult = state.searchResult,
                    isLoadingMore = state.isLoadingMore,
                    loadMore = {
                        onAction(MovieListAction.LoadMore)
                    }
                )
            }


        }
    }
}

@Composable
private fun EmptyResult(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = message, modifier = modifier.align(Alignment.Center)
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
    isLoadingMore: Boolean,
    loadMore: () -> Unit
) {
    searchResult.errorMessage?.let {
        EmptyResult(
            modifier = modifier,
            message = it
        )
    } ?: run {
        EndlessLazyColumn(
            modifier = modifier.fillMaxSize(),
            items = searchResult.movies,
            isLoadingMore = isLoadingMore,
            loadMoreItems = loadMore,
            content = { movie ->
                ListItem(
                    modifier = modifier.padding(bottom = 5.dp),
                    leadingContent = {
                        SubcomposeAsyncImage(
                            modifier = Modifier.width(100.dp),
                            loading = {
                                CircularProgressIndicator()
                            },
                            error = {
                                Image(painterResource(R.drawable.error_movie_poster), stringResource(R.string.errorMoviePosterPlaceholder))
                            },
                            model = movie.poster,
                            contentDescription = null,
                        )
                    },
                    overlineContent = { Text(text = movie.year) },
                    headlineContent = { Text(text = movie.title) },
                    supportingContent = {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Button(
                                modifier = Modifier.align(Alignment.BottomEnd),
                                onClick = { /** intentionally empty **/ }
                            ) {
                                Text(text = stringResource(R.string.movieDetail))
                            }
                        }
                    },
                    tonalElevation = 5.dp,
                    shadowElevation = 5.dp,
                )
            }
        )
    }
}

@Previews
@Composable
private fun MovieListContentPreview(
    @PreviewParameter(MovieListPreviewParameterProvider::class) state: MovieListPreviewState
) = MovieEnsembleTheme {
    Surface {
        MovieListContentInner(
            uiEvent = emptyFlow(),
            state = state.state,
            onAction = {}
        )
    }
}