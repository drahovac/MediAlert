package com.bobmitchigan.medialert

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
