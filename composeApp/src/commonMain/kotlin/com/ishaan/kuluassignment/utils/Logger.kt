package com.ishaan.kuluassignment.utils

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter

object Logger {
    val testLog = Logger(
        config = loggerConfigInit(platformLogWriter()),
        tag = "PUI"
    )

    val networkLog = Logger(
        config = loggerConfigInit(platformLogWriter()),
        tag = "NetworkLog"
    )

    val errorLog = Logger(
        config = loggerConfigInit(platformLogWriter()),
        tag = "Crash/Error"
    )
}