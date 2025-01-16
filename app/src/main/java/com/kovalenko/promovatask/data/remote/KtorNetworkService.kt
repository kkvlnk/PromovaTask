package com.kovalenko.promovatask.data.remote

import com.kovalenko.promovatask.data.remote.model.GenreDto
import com.kovalenko.promovatask.data.remote.model.GenresResponse
import com.kovalenko.promovatask.data.remote.model.MovieDto
import com.kovalenko.promovatask.data.remote.model.PaginatedResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// TODO: Move to BuildConfig
private const val BaseUrl = "https://api.themoviedb.org/3"

class KtorNetworkService : RemoteDataSource {
    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    private val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            url(BaseUrl)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
        install(ContentNegotiation) {
            json(json)
        }
    }

    override suspend fun getMovies(page: Int): PaginatedResponse<MovieDto> {
        return client
            .get("discover/movie")
            .body<PaginatedResponse<MovieDto>>()
    }

    override suspend fun getGenres(): List<GenreDto> {
        return client
            .get("genre/movie/list")
            .body<GenresResponse>()
            .genres
    }
}