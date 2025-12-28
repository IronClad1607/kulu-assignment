package com.ishaan.kuluassignment.features.movie_list.model

import com.ishaan.kuluassignment.base.BaseRepository
import com.ishaan.kuluassignment.db.MovieDatabase
import com.ishaan.kuluassignment.networking.ClientProvider

class MovieRepository(
    clientProvider: ClientProvider,
    movieDatabase: MovieDatabase
) : BaseRepository(clientProvider) {

}