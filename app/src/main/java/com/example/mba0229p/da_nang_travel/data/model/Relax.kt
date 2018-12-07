package com.example.mba0229p.da_nang_travel.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Relax(
        @Exclude
        var key: String = "",
        @set:PropertyName("address")
        @get:PropertyName("address")
        var address: String? = null,
        @set:PropertyName("description")
        @get:PropertyName("description")
        var description: String? = null,
        @set:PropertyName("lat")
        @get:PropertyName("lat")
        var lat: Double? = null,
        @set:PropertyName("lng")
        @get:PropertyName("lng")
        var lng: Double? = null,
        @set:PropertyName("nameLocation")
        @get:PropertyName("nameLocation")
        var nameLocation: String? = null,
        @set:PropertyName("phone")
        @get:PropertyName("phone")
        var phone: String? = null,
        var points: String? = null,
        var distance: Double? = null,
        var hour: Double? = null
) : Serializable {

    private fun fromDataSnapshot(dataSnapshot: DataSnapshot): Relax? {
        val relax = dataSnapshot.getValue(Relax::class.java)
        relax?.key = dataSnapshot.key.toString()
        return relax
    }

    fun fromDataSnapshotToList(dataSnapshot: DataSnapshot): MutableList<Relax> {
        val relaxList = mutableListOf<Relax>()
        dataSnapshot.children.forEach {
            Relax().fromDataSnapshot(it)?.run {
                relaxList.add(this)
            }
        }
        return relaxList
    }
}
