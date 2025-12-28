package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.features.movie_list.model.MovieRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::MovieRepository)
}