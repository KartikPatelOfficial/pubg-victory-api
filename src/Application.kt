package com.deucate

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
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
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.coroutines.async
import java.io.FileInputStream


@Suppress("BlockingMethodInNonBlockingContext")
fun Application.main() {

    val serviceAccount = FileInputStream("src/core/config.json")

    val options = FirestoreOptions.newBuilder()
        .setTimestampsInSnapshotsEnabled(true)
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    val db = options.service
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
            val id = call.request.queryParameters["id"] ?: "200"
            val event = async {
                server.geEventByID(id)
            }
            call.respond(event.await() ?: 404)
        }

        get("/all-rooms") {
            val events = async {
                server.getAllEvents()
            }
            call.respond(events.await() ?: 404)
        }

    }

}