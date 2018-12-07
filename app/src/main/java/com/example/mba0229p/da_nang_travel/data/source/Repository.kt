package com.example.mba0229p.da_nang_travel.data.source

import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.data.model.InfoHome
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.source.datasource.DataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.RemoteDataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

class Repository : DataSource {

    private val remoteDataSource = RemoteDataSource()

    // Get direction from Google API
    override fun getDrectionMap(origin: String, destination: String, key: String, sensor: String, mode: String): Single<DirectionMapResponse> =
            remoteDataSource.getDrectionMap(origin, destination, key, sensor, mode)

    // Get data home info from fire base
    internal fun homeInfoRepo(): Single<List<InfoHome>> {
        return Single.create<List<InfoHome>> { emit ->
            FirebaseDatabase.getInstance().reference
                    .child("infor")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            emit.onError(p0.toException())
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            emit.onSuccess(InfoHome().fromDataSnapshotToList(dataSnapshot))
                        }
                    })
        }
    }

    // Get data home event from fire base
    internal fun homeEventRepo(): Single<List<EventHome>> {
        return Single.create<List<EventHome>> { emit ->
            FirebaseDatabase.getInstance().reference
                    .child("event")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            emit.onError(p0.toException())
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            emit.onSuccess(EventHome().fromDataSnapshotToList(dataSnapshot))
                        }
                    })
        }
    }

    // Get data relax from fire base
    internal fun relaxRepo(): Single<List<Relax>> {
        return Single.create<List<Relax>> { emit ->
            FirebaseDatabase.getInstance().reference
                    .child("Relax")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            emit.onError(p0.toException())
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            emit.onSuccess(Relax().fromDataSnapshotToList(p0))
                        }
                    })
        }
    }
}
