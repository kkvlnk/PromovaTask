package com.kovalenko.promovatask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kovalenko.promovatask.domain.usecase.GetMoviesPagingUseCase
import com.kovalenko.promovatask.domain.usecase.SetMovieLikeStatusUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MoviesViewModel(
    private val moviesPagingUseCase: GetMoviesPagingUseCase,
    private val setLikeStatusUseCase: SetMovieLikeStatusUseCase
) : ViewModel() {
    val movies = moviesPagingUseCase().cachedIn(viewModelScope)

    fun performAction(action: MoviesAction) {
        when(action) {
            is MoviesAction.SetLikeStatus -> setLikeStatus(action.id, action.status)
        }
    }

    private fun setLikeStatus(id: Int, status: Boolean) {
        viewModelScope.launch {
            setLikeStatusUseCase(id, status).onFailure {
                // TODO: Handle error
            }
        }
    }
}

sealed class MoviesAction {
    data class SetLikeStatus(val id: Int, val status: Boolean): MoviesAction()
}