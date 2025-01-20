package com.kovalenko.promovatask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kovalenko.promovatask.R
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.domain.usecase.GetMoviesPagingUseCase
import com.kovalenko.promovatask.domain.usecase.ObserveGenresUseCase
import com.kovalenko.promovatask.domain.usecase.RefreshGenresUseCase
import com.kovalenko.promovatask.domain.usecase.SetMovieLikeStatusUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class MoviesViewModel(
    private val moviesPagingUseCase: GetMoviesPagingUseCase,
    private val setLikeStatusUseCase: SetMovieLikeStatusUseCase,
    private val refreshGenresUseCase: RefreshGenresUseCase,
    private val observeGenresUseCase: ObserveGenresUseCase
) : ViewModel() {
    private val _genresAvailable: Flow<Boolean> = observeGenresUseCase()
        .distinctUntilChanged()
        .map { it.isNotEmpty() }
        .onEach { if (!it) refreshGenres() }

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _errorMessage: MutableStateFlow<ErrorMessage?> = MutableStateFlow(null)

    val movies: Flow<PagingData<Movie>> = _genresAvailable
        .flatMapLatest { available ->
            moviesPagingUseCase().takeIf { available } ?: flowOf(PagingData.empty())
        }
        .cachedIn(viewModelScope)

    val uiState: StateFlow<MoviesScreenUiState> = combine(
        _genresAvailable,
        _isLoading,
        _errorMessage
    ) { genresAvailable, isLoading, errorMessage ->
        if (genresAvailable) {
            MoviesScreenUiState.Movies(isLoading = isLoading, errorMessage = errorMessage)
        } else {
            if (isLoading) {
                MoviesScreenUiState.Loading
            } else {
                MoviesScreenUiState.EmptyGenres
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = MoviesScreenUiState.Loading
        )

    fun performAction(action: MoviesAction) {
        when (action) {
            is MoviesAction.SetLikeStatus -> setLikeStatus(action.id, action.status)
            MoviesAction.RefreshGenres -> refreshGenres()
            MoviesAction.DismissMessage -> dismissMessage()
        }
    }

    private fun refreshGenres() {
        viewModelScope.launch {
            _isLoading.update { true }
            refreshGenresUseCase()
                .onFailure {
                    _errorMessage.update { ErrorMessage.ResourceMessage(R.string.error_unknown) }
                }
            _isLoading.update { false }
        }
    }

    private fun setLikeStatus(id: Int, status: Boolean) {
        viewModelScope.launch {
            setLikeStatusUseCase(movieId = id, liked = status)
                .onFailure {
                    _errorMessage.update { ErrorMessage.ResourceMessage(R.string.error_unknown) }
                }
        }
    }

    private fun dismissMessage() {
        _errorMessage.update { null }
    }
}

sealed class MoviesScreenUiState {
    data class Movies(
        val isLoading: Boolean = false,
        val errorMessage: ErrorMessage? = null
    ) : MoviesScreenUiState()

    data class Error(val errorMessage: ErrorMessage?) : MoviesScreenUiState()
    data object EmptyGenres: MoviesScreenUiState()
    data object Loading : MoviesScreenUiState()
}

sealed class MoviesAction {
    data class SetLikeStatus(val id: Int, val status: Boolean) : MoviesAction()
    data object RefreshGenres: MoviesAction()
    data object DismissMessage: MoviesAction()
}