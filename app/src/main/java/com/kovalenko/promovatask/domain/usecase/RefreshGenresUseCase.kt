package com.kovalenko.promovatask.domain.usecase

import com.kovalenko.promovatask.domain.repository.MovieRepository
import org.koin.core.annotation.Factory

@Factory
class RefreshGenresUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshGenres()
    }
}