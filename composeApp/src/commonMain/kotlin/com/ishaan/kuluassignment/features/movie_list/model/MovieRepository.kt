package com.ishaan.kuluassignment.features.movie_list.model

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.ishaan.kuluassignment.base.BaseRepository
import com.ishaan.kuluassignment.db.MovieDatabase
import com.ishaan.kuluassignment.networking.ClientProvider
import com.ishaan.kuluassignment.networking.NetworkResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class MovieRepository(
    clientProvider: ClientProvider,
    private val movieDatabase: MovieDatabase
) : BaseRepository(clientProvider) {

    val moviesFlow = movieDatabase.appDatabaseQueries
        .selectAllMovies()
        .asFlow()
        .mapToList(Dispatchers.IO)

    fun getLastLoadedPage(): Long {
        return movieDatabase.appDatabaseQueries.getLastPage().executeAsOne().MAX ?: 0
    }

    suspend fun fetchAndSaveMovies(page: Int): Result<Unit> {
        val response = makeNetworkCall<GetTrendingMoviesResponse>(
            method = HttpMethod.Get,
            endpoint = "trending/movie/week",
            queryParams = mapOf("page" to page)
        )

        return when (response) {
            is NetworkResponse.Error -> {
                Result.failure(Exception(response.message))
            }

            is NetworkResponse.Success<GetTrendingMoviesResponse> -> {
                val movies = response.data.movies

                movieDatabase.transaction {
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
            }
        }
    }

    suspend fun clearCache() {
        movieDatabase.appDatabaseQueries.deleteAllMovies()
    }
}