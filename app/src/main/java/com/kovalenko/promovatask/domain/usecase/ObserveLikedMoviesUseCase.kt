package com.kovalenko.promovatask.domain.usecase

import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveLikedMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getLikedMovies()
    }
}