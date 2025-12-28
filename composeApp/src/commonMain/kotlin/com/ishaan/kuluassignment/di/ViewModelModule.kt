package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.features.movie_detail.viewmodel.MovieDetailViewModel
import com.ishaan.kuluassignment.features.movie_list.viewmodel.MovieListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MovieListViewModel)
    viewModelOf(::MovieDetailViewModel)
}