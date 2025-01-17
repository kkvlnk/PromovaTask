package com.kovalenko.promovatask.domain.usecase

import androidx.paging.PagingData
import com.kovalenko.promovatask.domain.model.Movie
import com.kovalenko.promovatask.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetMoviesPagingUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<PagingData<Movie>> {
        return repository.getMoviesPaging()
    }
}