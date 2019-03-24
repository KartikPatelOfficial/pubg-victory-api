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

    override suspend fun geEventByID(id: String): Event? {
        return try {
            val document = db.collection("Rooms").document(id).get().get()
            document.toObject(Event::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllEvents(): ArrayList<Event>? {
        val documents = db.collection("Rooms").get().get().documents
        val rooms = ArrayList<Event>()

        for (document in documents) {
            rooms.add(document.toObject(Event::class.java))
        }

        return if (rooms.isEmpty()) {
            null
        } else {
            rooms
        }
    }

    override suspend fun deleteEvent(id: String, callback: (response: Void?, error: String?) -> Unit) {
        db.collection("Rooms").document(id).delete()
    }


    override suspend fun deleteAllEvents(callback: (response: Void?, error: String?) -> Unit) {
        callback.invoke(null, "500 internal server error")
    }

}