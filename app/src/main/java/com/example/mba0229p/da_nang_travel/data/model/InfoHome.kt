package com.example.mba0229p.da_nang_travel.data.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable

data class InfoHome(
        @Exclude
        var key: String = "",
        @set:PropertyName("nameLocation")
        @get:PropertyName("nameLocation")
        var nameLocation: String? = null,
        @set:PropertyName("image")
        @get:PropertyName("image")
        var image: List<String>? = null,
        @set:PropertyName("content")
        @get:PropertyName("content")
        var content: String? = null
) : Serializable {

    private fun fromDataSnapshot(dataSnapshot: DataSnapshot): InfoHome? {
        val infoHome = dataSnapshot.getValue(InfoHome::class.java)
        infoHome?.key = dataSnapshot.key.toString()
        return infoHome
    }

    fun fromDataSnapshotToList(dataSnapshot: DataSnapshot): MutableList<InfoHome> {
        val infoHomes = mutableListOf<InfoHome>()
        dataSnapshot.children.forEach {
            InfoHome().fromDataSnapshot(it)?.run {
                infoHomes.add(this)
            }
        }
        return infoHomes
    }
}
