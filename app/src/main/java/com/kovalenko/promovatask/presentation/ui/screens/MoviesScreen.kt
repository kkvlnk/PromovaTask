package com.kovalenko.promovatask.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.presentation.viewmodel.MoviesAction
import com.kovalenko.promovatask.presentation.viewmodel.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesRoute(
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()
    MoviesScreen(
        movies = movies,
        performAction = viewModel::performAction,
        modifier = modifier
    )
}

@Composable
fun MoviesScreen(
    movies: LazyPagingItems<Movie>,
    performAction: (MoviesAction) -> Unit,
    modifier: Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                count = movies.itemCount,
                key = movies.itemKey(key = { it.id }),
                contentType = movies.itemContentType()
            ) { index ->
                val movie = movies[index]
                if (movie != null) {
                    // TODO: Implement MovieItem
                }
            }
        }
    }
}