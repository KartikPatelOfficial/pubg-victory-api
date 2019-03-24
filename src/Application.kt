package com.deucate

import com.deucate.model.Room
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import java.text.DateFormat
import java.io.FileInputStream


fun Application.main() {

    val serviceAccount = FileInputStream("src/config.json")

    val options = FirestoreOptions.newBuilder()
        .setTimestampsInSnapshotsEnabled(true)
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    val db = options.service

    install(DefaultHeaders)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            val query = db.collection("Rooms").get()

            @Suppress("BlockingMethodInNonBlockingContext")
            val querySnapshot = query.get()
            val documents = querySnapshot.documents
            for (document in documents) {
                val room = document.toObject(Room::class.java)
                call.respond(room)
            }
        }
    }
}
