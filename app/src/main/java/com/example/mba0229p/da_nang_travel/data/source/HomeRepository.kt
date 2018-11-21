package com.example.mba0229p.da_nang_travel.data.source

import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.data.model.InfoHome
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

class HomeRepository {

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
}