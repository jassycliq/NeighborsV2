package com.playbowdogs.neighbors.data.api

import okhttp3.Credentials
import okhttp3.Interceptor

class BasicAuthInterceptor(userID: String, apiKey: String) : Interceptor {
    private var credentials: String = Credentials.basic(userID, apiKey)

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}
