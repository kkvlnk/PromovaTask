package com.kovalenko.promovatask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kovalenko.promovatask.domain.usecase.GetMoviesPagingUseCase
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MoviesViewModel(
    private val moviesPagingUseCase: GetMoviesPagingUseCase
) : ViewModel() {
    val movies = moviesPagingUseCase().cachedIn(viewModelScope)

    fun performAction(action: MoviesAction) {
        when(action) {
            is MoviesAction.SetLikeStatus -> setLikeStatus(action.id, action.status)
        }
    }

    private fun setLikeStatus(id: Int, status: Boolean) {
        TODO("Not yet implemented")
    }
}

sealed class MoviesAction {
    data class SetLikeStatus(val id: Int, val status: Boolean): MoviesAction()
}