package com.yan.feracode.spotify.data.api.retrofit



import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

import com.yan.feracode.spotify.data.api.Constants.ACCESS_TOKEN
import com.yan.feracode.spotify.data.api.Constants.API_KEY

internal class ApiInterceptor : Interceptor {

    var AUTH_TOKEN: String? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        val request = original.newBuilder().header(API_KEY, ACCESS_TOKEN + AUTH_TOKEN!!)
                .method(original.method(), original.body()).build()
        return chain.proceed(request)
    }

}