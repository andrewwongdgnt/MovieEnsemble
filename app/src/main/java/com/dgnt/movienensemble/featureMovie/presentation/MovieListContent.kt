package com.dgnt.movienensemble.featureMovie.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.movienensemble.R
import com.dgnt.movienensemble.core.presentation.preview.Previews
import com.dgnt.movienensemble.ui.theme.MovieEnsembleTheme

@Composable
fun MovieListContent(
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    MovieListContentInner(
        searchQuery = searchQuery,
        onSearch = viewModel::onSearch
    )
}

@Composable
private fun MovieListContentInner(
    searchQuery: String,
    onSearch: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = stringResource(R.string.searchMovie))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

            }
        }
//        if(state.isLoading) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }
    }
}

@Previews
@Composable
private fun MovieListContentPreview(
    @PreviewParameter(MovieListPreviewParameterProvider::class) state: MovieListPreviewState
) = MovieEnsembleTheme {
    Surface {
        MovieListContentInner(
            searchQuery = state.searchQuery,
            onSearch = {}
        )
    }
}