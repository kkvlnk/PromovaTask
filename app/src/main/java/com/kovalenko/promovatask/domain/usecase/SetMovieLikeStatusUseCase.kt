package com.kovalenko.promovatask.domain.usecase

import com.kovalenko.promovatask.domain.repository.MovieRepository
import org.koin.core.annotation.Factory

@Factory
class SetMovieLikeStatusUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int, liked: Boolean): Result<Unit> {
        return repository.updateLikedStatus(movieId = movieId, liked = liked)
    }
}