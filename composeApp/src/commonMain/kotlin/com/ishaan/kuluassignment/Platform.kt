package com.ishaan.kuluassignment

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform