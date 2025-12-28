package com.ishaan.kuluassignment.di

import com.ishaan.kuluassignment.db.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    singleOf(::DatabaseDriverFactory)
}