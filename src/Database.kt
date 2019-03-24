package com.deucate

import io.ktor.http.HttpStatusCode

interface Database {

    suspend fun geEventByID(id: String): EventData

    suspend fun getAllEvents(): EventData

    suspend fun deleteEvent(id: String): EventData

    suspend fun deleteAllEvents(): EventData

}

data class EventData(
    val response: Any?,
    val status: HttpStatusCode
)