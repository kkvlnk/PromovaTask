package com.kovalenko.promovatask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.domain.usecase.ObserveLikedMoviesUseCase
import com.kovalenko.promovatask.domain.usecase.SetMovieLikeStatusUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoritesViewModel(
    private val observeLikedMoviesUseCase: ObserveLikedMoviesUseCase,
    private val setLikeStatusUseCase: SetMovieLikeStatusUseCase
) : ViewModel() {
    private val _movies = observeLikedMoviesUseCase().distinctUntilChanged()
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _errorMessage: MutableStateFlow<ErrorMessage?> = MutableStateFlow(null)

    val uiState: StateFlow<FavoritesUiState> = combine(
        _movies, _isLoading, _errorMessage
    ) { movies, isLoading, errorMessage ->
        FavoritesUiState(
            movies = movies.toImmutableList(),
            errorMessage = errorMessage,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = FavoritesUiState()
    )

    fun performAction(action: FavoritesAction) {
        when (action) {
            is FavoritesAction.SetLikeStatus -> setLikeStatus(action.id, action.status)
            FavoritesAction.DismissMessage -> dismissMessage()
        }
    }

    private fun dismissMessage() {
        _errorMessage.update { null }
    }

    private fun setLikeStatus(id: Int, status: Boolean) {
        viewModelScope.launch {
            setLikeStatusUseCase(movieId = id, liked = status)
                .onFailure {
                    _errorMessage.update { ErrorMessage.ResourceMessage(R.string.error_unknown) }
                }
        }
    }
}

data class FavoritesUiState(
    val movies: ImmutableList<Movie> = persistentListOf(),
    val errorMessage: ErrorMessage? = null,
    val isLoading: Boolean = false
)

sealed class FavoritesAction {
    data class SetLikeStatus(val id: Int, val status: Boolean) : FavoritesAction()
    data object DismissMessage : FavoritesAction()
}