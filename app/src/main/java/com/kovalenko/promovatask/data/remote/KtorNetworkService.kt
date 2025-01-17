package com.kovalenko.promovatask.data.remote

import com.kovalenko.promovatask.BuildConfig
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
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber

private const val BaseUrl = BuildConfig.TMDB_BASE_URL

@Single
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
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.v("Ktor => $message")
                }
            }
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_TOKEN}")
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
            .get("discover/movie") {
                url.parameters.append("page", page.toString())
            }
            .body<PaginatedResponse<MovieDto>>()
    }

    override suspend fun getGenres(): List<GenreDto> {
        return client
            .get("genre/movie/list")
            .body<GenresResponse>()
            .genres
    }
}