package com.deucate.model

import com.google.cloud.Timestamp


const val NotFound = "Not Found"

data class Event(
    var AuthorID: String = NotFound,
    val AuthorName: String = NotFound,
    val EntryFees: Long = 0,
    val GameDescription: String = NotFound,
    val Image: String = NotFound,
    val Map: Long = 0,
    val Price: Long = 0,
    val RoomID: String = NotFound,
    val Teams: Long = 0,
    val Time: Timestamp = Timestamp.now(),
    val Title: String = NotFound
)