package com.kovalenko.promovatask.domain.usecase

import com.kovalenko.promovatask.domain.model.Genre
import com.kovalenko.promovatask.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveGenresUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Genre>> {
        return repository.observeGenres()
    }
}