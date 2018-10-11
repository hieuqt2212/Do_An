package com.example.mba0229p.da_nang_travel.data.source.remote.network

import com.example.mba0229p.da_nang_travel.data.source.util.BaseRxCallAdapterWrapper
import okhttp3.ResponseBody
import retrofit2.*
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class RxCallAdapterWrapper<R>(type: Type, retrofit: Retrofit, wrapped: CallAdapter<R, *>?) : BaseRxCallAdapterWrapper<R>(type, retrofit, wrapped) {

    override fun convertRetrofitExceptionToCustomException(throwable: Throwable, retrofit: Retrofit): Throwable {

        if (throwable is HttpException) {
            val converter: Converter<ResponseBody, ApiException> = retrofit.responseBodyConverter(ApiException::class.java, arrayOfNulls<Annotation>(0))
            val response: Response<*>? = throwable.response()
            when (response?.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> response.errorBody()?.let {
                    return converter.convert(it).apply {
                        statusCode = HttpsURLConnection.HTTP_UNAUTHORIZED
                    }
                }

                HttpsURLConnection.HTTP_INTERNAL_ERROR -> response.errorBody()?.let {
                    return converter.convert(it)
                }

                HttpsURLConnection.HTTP_BAD_REQUEST -> response.errorBody()?.let {
                    return converter.convert(it).apply {
                        statusCode = HttpsURLConnection.HTTP_BAD_REQUEST
                    }
                }

                HttpsURLConnection.HTTP_NOT_FOUND -> response.errorBody()?.let {
                    val notFoundMessage = "不正なリクエストです。"
                    val apiException = ApiException("", "NOT_FOUND", mutableListOf(ErrorObjects("404", mutableListOf(notFoundMessage))))
                    apiException.statusCode = HttpsURLConnection.HTTP_NOT_FOUND
                    return apiException
                }
            }
        }

        if (throwable is UnknownHostException) {
            // Set message error of this case in activity extension
            val noNetworkErrorMessage = "インターネット接続がありません。\n通信環境をご確認の上、コードを生成し直してください。"
            val apiException = ApiException("", "NETWORK_ERROR_CODE", mutableListOf(ErrorObjects("NoConnectionError", mutableListOf(noNetworkErrorMessage))))
            apiException.statusCode = ApiException.NETWORK_ERROR_CODE
            return apiException
        }

        if (throwable is SocketTimeoutException) {
            val timeoutErrorMessage = "接続に失敗しました。\n通信環境を確認してリトライしてください。"
            val apiException = ApiException("", "TIME_OUT", mutableListOf(ErrorObjects("RequestTimeout", mutableListOf(timeoutErrorMessage))))
            apiException.statusCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT
            return apiException
        }

        return throwable
    }

    override fun createExceptionForSuccessResponse(response: Any?): Throwable? = super.createExceptionForSuccessResponse(response)
}
