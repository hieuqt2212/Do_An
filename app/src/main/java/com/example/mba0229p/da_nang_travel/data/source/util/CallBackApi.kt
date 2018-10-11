package com.example.mba0229p.da_nang_travel.data.source.util

import com.example.mba0229p.da_nang_travel.data.source.remote.network.CustomCallback
import retrofit2.Call
import retrofit2.Response
class CallBackApi private constructor() {
    companion object {
        internal fun <T> callback(
                onSuccess: (Call<T>, Response<T>) -> Unit = { _, _ -> },
                onError: (Throwable) -> Unit = { }
        ): CustomCallback<T> = object : CustomCallback<T> {
            override fun success(call: Call<T>, response: Response<T>) {
                onSuccess.invoke(call, response)
            }

            override fun unauthenticated(t: Throwable) {
                onError.invoke(t)
            }

            override fun clientError(t: Throwable) {
                onError.invoke(t)
            }

            override fun serverError(t: Throwable) {
                onError.invoke(t)
            }

            override fun networkError(e: Throwable) {
                onError.invoke(e)
            }

            override fun unexpectedError(t: Throwable) {
                onError.invoke(t)
            }
        }
    }
}
