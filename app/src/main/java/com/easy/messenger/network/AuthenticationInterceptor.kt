package com.easy.messenger.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeader = chain.request().newBuilder()
            .addHeader("oauth", "0123456789")
            .build()
        return chain.proceed(requestWithHeader)
    }
}
