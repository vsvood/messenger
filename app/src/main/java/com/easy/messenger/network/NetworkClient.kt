package com.easy.messenger.network

import com.easy.messenger.BASE_SERVER_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthenticationInterceptor())
        .build()

    val chatApiService: ChatApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }
}
