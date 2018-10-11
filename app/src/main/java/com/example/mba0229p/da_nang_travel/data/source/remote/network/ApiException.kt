package com.example.mba0229p.da_nang_travel.data.source.remote.network

import com.google.gson.annotations.SerializedName

/**
 * Use this file to handle error from api
 */
data class ApiException(
        @SerializedName("object") val objectError: String,
        @SerializedName("type") val type: String,
        @SerializedName("errors") val errors: MutableList<ErrorObjects>) : Throwable() {

    companion object {
        internal const val FORCE_UPDATE_ERROR_CODE = 426
        internal const val NETWORK_ERROR_CODE = 700
    }

    var statusCode: Int? = null
}

data class ErrorObjects(
        @SerializedName("code") val code: String,
        @SerializedName("messages") val messages: MutableList<String>
)
