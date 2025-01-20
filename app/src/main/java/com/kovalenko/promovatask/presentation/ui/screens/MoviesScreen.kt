package com.kovalenko.promovatask.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.presentation.ui.components.MovieListItem
import com.kovalenko.promovatask.presentation.viewmodel.MoviesAction
import com.kovalenko.promovatask.presentation.viewmodel.MoviesScreenUiState
import com.kovalenko.promovatask.presentation.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesRoute(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    MoviesScreen(
        uiState = uiState,
        movies = movies,
        performAction = viewModel::performAction,
        modifier = modifier
    )
}

@Composable
fun MoviesScreen(
    uiState: MoviesScreenUiState,
    performAction: (MoviesAction) -> Unit,
    movies: LazyPagingItems<Movie>,
    modifier: Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (uiState) {
                MoviesScreenUiState.Loading -> LoadingContent()
                MoviesScreenUiState.EmptyGenres -> EmptyGenresContent(
                    refresh = { performAction(MoviesAction.RefreshGenres) }
                )

                is MoviesScreenUiState.Error -> TODO()

                is MoviesScreenUiState.Movies -> MoviesContent(
                    movies = movies,
                    setLikeStatus = { id, likeStatus ->
                        performAction(MoviesAction.SetLikeStatus(id, likeStatus))
                    }
                )
            }
        }
    }
}

@Composable
fun MoviesContent(
    movies: LazyPagingItems<Movie>,
    setLikeStatus: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey(key = { it.id }),
            contentType = movies.itemContentType()
        ) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieListItem(movie, setLikeStatus = setLikeStatus)
            }
        }
    }
}

@Composable
fun EmptyGenresContent(modifier: Modifier = Modifier, refresh: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.empty_genres_message), textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            Button(onClick = refresh) {
                Text(text = stringResource(R.string.refresh))
            }
        }
    }
}

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}