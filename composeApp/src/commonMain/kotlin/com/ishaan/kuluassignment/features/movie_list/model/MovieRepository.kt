package com.ishaan.kuluassignment.features.movie_list.model

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.ishaan.kuluassignment.base.BaseRepository
import com.ishaan.kuluassignment.db.MovieDatabase
import com.ishaan.kuluassignment.db.MovieEntity
import com.ishaan.kuluassignment.networking.ClientProvider
import com.ishaan.kuluassignment.networking.NetworkResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class MovieRepository(
    clientProvider: ClientProvider,
    private val movieDatabase: MovieDatabase
) : BaseRepository(clientProvider) {

    val moviesFlow = movieDatabase.appDatabaseQueries
        .selectAllMovies()
        .asFlow()
        .mapToList(Dispatchers.IO)

    suspend fun getMovieById(movieId: Long): MovieEntity? {
        return withContext(Dispatchers.IO) {
            movieDatabase.appDatabaseQueries
                .getMovieById(movieId)
                .executeAsOneOrNull()
        }
    }

    fun getLastLoadedPage(): Long {
        return movieDatabase.appDatabaseQueries.getLastPage().executeAsOne().MAX ?: 0
    }

    suspend fun searchMovies(query: String, page: Int, isRefresh: Boolean = false): Result<Unit> {
        return fetchMovies(
            endpoint = "search/movie",
            queryParams = mapOf(
                "query" to query,
                "page" to page,
                "include_adult" to true
            ),
            page = page,
            isRefresh = isRefresh
        )
    }

    suspend fun fetchAndSaveMovies(page: Int, isRefresh: Boolean = false): Result<Unit> {
        return fetchMovies(
            endpoint = "trending/movie/week",
            queryParams = mapOf("page" to page),
            page = page,
            isRefresh = isRefresh
        )
    }

    private suspend fun fetchMovies(
        endpoint: String,
        queryParams: Map<String, Any>,
        page: Int,
        isRefresh: Boolean
    ): Result<Unit> {
        val response = makeNetworkCall<GetMoviesResponse>(
            method = HttpMethod.Get,
            endpoint = endpoint,
            queryParams = queryParams
        )

        return when (response) {
            is NetworkResponse.Error -> {
                Result.failure(Exception(response.message))
            }

            is NetworkResponse.Success<GetMoviesResponse> -> {
                val movies = response.data.movies

                try {
                    movieDatabase.transaction {
                        if (isRefresh) {
                            movieDatabase.appDatabaseQueries.deleteAllMovies()
                        }

                        val startOrder = (page - 1) * 20
                        movies.forEachIndexed { index, movie ->
                            movieDatabase.appDatabaseQueries.insertMovie(
                                movieId = movie.id.toLong(),
                                title = movie.title,
                                overview = movie.overview,
                                posterPath = movie.posterPath,
                                backdropPath = movie.backdropPath,
                                page = page.toLong(),
                                sortOrder = (startOrder + index).toLong()
                            )
                        }
                    }
                    Result.success(Unit)
                } catch (e: Exception) {
                    println("Database error: ${e.message}")
                    Result.failure(e)
                }
            }
        }
    }

    suspend fun clearCache() {
        movieDatabase.appDatabaseQueries.deleteAllMovies()
    }
}