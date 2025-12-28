package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.db.DatabaseDriverFactory
import com.ishaan.kuluassignment.db.MovieDatabase
import com.ishaan.kuluassignment.networking.ClientProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val dataModule = module {
    singleOf(::ClientProvider)
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        MovieDatabase(driver)
    }
}