package com.deucate

import com.deucate.model.Event

interface Database {

    suspend fun geEventByID(id:String): Event?

    suspend fun getAllEvents(): ArrayList<Event>?

    suspend fun deleteEvent(id: String, callback: (response: Void?, error: String?) -> Unit)

    suspend fun deleteAllEvents(callback: (response: Void?, error: String?) -> Unit)

}