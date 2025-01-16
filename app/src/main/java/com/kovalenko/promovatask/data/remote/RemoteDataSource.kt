package com.kovalenko.promovatask.data.remote

import com.kovalenko.promovatask.data.remote.model.MovieDto
import com.kovalenko.promovatask.data.remote.model.PaginatedResponse

interface RemoteDataSource {
    suspend fun getMovies(page: Int): PaginatedResponse<MovieDto>
}