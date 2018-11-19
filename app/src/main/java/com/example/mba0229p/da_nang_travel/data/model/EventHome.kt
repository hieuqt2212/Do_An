package com.example.mba0229p.da_nang_travel.data.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable

data class EventHome(
        @Exclude
        var key: String = "",
        @set:PropertyName("nameEvent")
        @get:PropertyName("nameEvent")
        var nameEvent: String? = null,
        @set:PropertyName("image")
        @get:PropertyName("image")
        var image: List<String>? = null,
        @set:PropertyName("content")
        @get:PropertyName("content")
        var content: String? = null
) : Serializable {

    private fun fromDataSnapshot(dataSnapshot: DataSnapshot): EventHome? {
        val eventHome = dataSnapshot.getValue(EventHome::class.java)
        eventHome?.key = dataSnapshot.key.toString()
        return eventHome
    }

    fun fromDataSnapshotToList(dataSnapshot: DataSnapshot): MutableList<EventHome> {
        val eventHomes = mutableListOf<EventHome>()
        dataSnapshot.children.forEach {
            EventHome().fromDataSnapshot(it)?.run {
                eventHomes.add(this)
            }
        }
        return eventHomes
    }
}
