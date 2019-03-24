package com.deucate.model

import io.ktor.http.HttpStatusCode

data class Response(
    val status: HttpStatusCode
)