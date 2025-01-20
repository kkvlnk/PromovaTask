package com.kovalenko.promovatask.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kovalenko.promovatask.presentation.ui.components.MovieListItem
import com.kovalenko.promovatask.presentation.viewmodel.ErrorMessage
import com.kovalenko.promovatask.presentation.viewmodel.FavoritesViewModel
import com.kovalenko.promovatask.presentation.viewmodel.FavoritesAction
import com.kovalenko.promovatask.presentation.viewmodel.FavoritesUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesRoute(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        uiState = uiState,
        performAction = viewModel::performAction,
        modifier = modifier
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    performAction: (FavoritesAction) -> Unit,
    modifier: Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val message = when(val error = uiState.errorMessage) {
        is ErrorMessage.ResourceMessage -> stringResource(error.message)
        is ErrorMessage.StringMessage -> error.message
        null -> null
    }

    LaunchedEffect(snackbarHostState, message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message = message, withDismissAction = true)
            performAction(FavoritesAction.DismissMessage)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            items(items = uiState.movies) { movie ->
                MovieListItem(movie = movie) { id, likeStatus ->
                    performAction(FavoritesAction.SetLikeStatus(id, likeStatus))
                }
            }
        }
    }
}
