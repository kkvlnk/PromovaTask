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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.presentation.ui.components.MovieErrorItem
import com.kovalenko.promovatask.presentation.ui.components.MovieListItem
import com.kovalenko.promovatask.presentation.ui.components.MovieLoadingItem
import com.kovalenko.promovatask.presentation.viewmodel.ErrorMessage
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
    val snackbarHostState = remember { SnackbarHostState() }

    val message: String? = when(uiState) {
        MoviesScreenUiState.EmptyGenres -> null
        MoviesScreenUiState.Loading -> null
        is MoviesScreenUiState.Error -> null
        is MoviesScreenUiState.Movies -> {
            when (val error = uiState.errorMessage) {
                is ErrorMessage.ResourceMessage -> stringResource(error.message)
                is ErrorMessage.StringMessage -> error.message
                null -> null
            }
        }
    }

    LaunchedEffect(snackbarHostState, message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message = message, withDismissAction = true)
            performAction(MoviesAction.DismissMessage)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (uiState) {
                MoviesScreenUiState.Loading -> LoadingContent()
                MoviesScreenUiState.EmptyGenres -> ErrorContent(
                    text = stringResource(R.string.empty_genres_message),
                    retry = { performAction(MoviesAction.RefreshGenres) }
                )

                is MoviesScreenUiState.Error -> ErrorContent(
                    text = when (val message = uiState.errorMessage) {
                        is ErrorMessage.ResourceMessage -> stringResource(message.message)
                        is ErrorMessage.StringMessage -> message.message
                        null -> stringResource(R.string.error_unknown)
                    }
                )

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
    if (movies.loadState.refresh is LoadState.Loading && movies.itemCount == 0) {
        LoadingContent()
    } else {
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

            when (movies.loadState.append) {
                is LoadState.Error -> item { MovieErrorItem(retry = movies::retry) }
                LoadState.Loading -> item { MovieLoadingItem() }
                else -> Unit
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

@Composable
fun ErrorContent(
    text: String,
    modifier: Modifier = Modifier,
    retry: (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            if (retry != null) {
                Spacer(Modifier.height(12.dp))
                Button(onClick = retry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}