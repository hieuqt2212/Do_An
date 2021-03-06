package com.example.mba0229p.da_nang_travel.data.source

import android.content.Context
import android.net.ConnectivityManager
import com.example.mba0229p.da_nang_travel.data.model.EventHome
import com.example.mba0229p.da_nang_travel.data.model.InfoHome
import com.example.mba0229p.da_nang_travel.data.model.Relax
import com.example.mba0229p.da_nang_travel.data.model.event.NetworkErrorEvent
import com.example.mba0229p.da_nang_travel.data.source.datasource.DataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.RemoteDataSource
import com.example.mba0229p.da_nang_travel.data.source.remote.network.response.direction_map.DirectionMapResponse
import com.google.firebase.database.*
import io.reactivex.Single
import jp.co.netprotections.atoneregi.data.source.remote.network.RxBus

class Repository : DataSource {

    private val remoteDataSource = RemoteDataSource()

    // Get direction from Google API
    override fun getDrectionMap(origin: String, destination: String, key: String): Single<DirectionMapResponse> =
            remoteDataSource.getDrectionMap(origin, destination, key)

    // Get data home event from fire base
    internal fun homeEventRepo(context: Context): Single<List<EventHome>> {
        return Single.create<List<EventHome>> { emit ->
            val firebaseReference = FirebaseDatabase.getInstance().reference.child("event")
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    emit.onError(p0.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    emit.onSuccess(EventHome().fromDataSnapshotToList(dataSnapshot))
                }
            }
            firebaseReference.addListenerForSingleValueEvent(valueEventListener)

            checkNetwork(context, firebaseReference)
        }
    }

    // Get data relax from fire base
    internal fun relaxRepo(context: Context): Single<List<Relax>> {
        return Single.create<List<Relax>> { emit ->
            val firebaseReference = FirebaseDatabase.getInstance().reference.child("relax")
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    emit.onError(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    emit.onSuccess(Relax().fromDataSnapshotToList(p0))
                }
            }
            firebaseReference.addListenerForSingleValueEvent(valueEventListener)

            checkNetwork(context, firebaseReference)
        }
    }

    // Get data eat from fire base
    internal fun eatRepo(context: Context): Single<List<Relax>> {
        return Single.create<List<Relax>> { emit ->
            val firebaseReference = FirebaseDatabase.getInstance().reference.child("eat")
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    emit.onError(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    emit.onSuccess(Relax().fromDataSnapshotToList(p0))
                }
            }
            firebaseReference.addListenerForSingleValueEvent(valueEventListener)

            checkNetwork(context, firebaseReference)
        }
    }

    // Get data Guest House from fire base
    internal fun guestHouseRepo(context: Context): Single<List<Relax>> {
        return Single.create<List<Relax>> { emit ->
            val firebaseReference = FirebaseDatabase.getInstance().reference.child("guestHouse")
            val valueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    emit.onError(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    emit.onSuccess(Relax().fromDataSnapshotToList(p0))
                }
            }
            firebaseReference.addListenerForSingleValueEvent(valueEventListener)

            checkNetwork(context, firebaseReference)
        }
    }

    private fun checkNetwork(context: Context, firebaseReference: DatabaseReference) {
        // Handle network error
        if ((context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo == null) {
            RxBus.publishToPublishSubject(NetworkErrorEvent())
            firebaseReference.removeValue()
        }
    }
}
