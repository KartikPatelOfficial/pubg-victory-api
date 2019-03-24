package com.deucate

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.async

@Suppress("BlockingMethodInNonBlockingContext")
fun Application.main() {

    val server = Server()

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
    install(DefaultHeaders)
    install(ContentNegotiation) {
        gson()
    }

    routing {

        get("/room") {
            val id = call.request.queryParameters["id"] ?: ""
            val event = async {
                server.geEventByID(id)
            }
            call.respond(event.await())
        }

        get("/all-rooms") {
            val events = async {
                server.getAllEvents()
            }
            call.respond(events.await())
        }

        delete("/deleteAll") {
            val result = async {
                server.deleteAllEvents()
            }
            call.respond(result.await())
        }

    }

}