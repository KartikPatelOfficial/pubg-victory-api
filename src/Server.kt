package com.deucate

import com.deucate.model.Event
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import io.ktor.application.Application
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.FileInputStream


private const val portArgsName = "--server.port"
private const val defaultPort = 8080

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty() && args[0].startsWith(portArgsName)) {
        args[0].split("=").last().trim().toInt()
    } else defaultPort

    embeddedServer(Netty, port, module = Application::main).start(wait = true)
}

private val serviceAccount = FileInputStream("src/core/config.json")

private val options = FirestoreOptions.newBuilder()
    .setTimestampsInSnapshotsEnabled(true)
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .build()

private val db = options.service

class Server : Database {

    override suspend fun geEventByID(id: String): EventData {
        return try {
            val document = db.collection("Rooms").document(id).get().get()
            EventData(document.toObject(Event::class.java), HttpStatusCode.OK)
        } catch (e: Exception) {
            EventData(null, HttpStatusCode.BadRequest)
        }
    }

    override suspend fun getAllEvents(): EventData {
        val documents = db.collection("Rooms").get().get().documents
        val rooms = ArrayList<Event>()

        for (document in documents) {
            rooms.add(document.toObject(Event::class.java))
        }

        return if (rooms.isEmpty()) {
            EventData(null, HttpStatusCode.BadRequest)
        } else {
            EventData(rooms, HttpStatusCode.OK)

        }
    }

    override suspend fun deleteEvent(id: String): EventData {
        return EventData(null, HttpStatusCode.Forbidden)
    }

    override suspend fun deleteAllEvents(): EventData {
        return EventData(null, HttpStatusCode.Forbidden)
    }

}