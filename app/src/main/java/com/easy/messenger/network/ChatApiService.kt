package com.easy.messenger.network

import com.easy.messenger.network.responses.GetChatsResponse
import com.easy.messenger.network.responses.GetMessagesResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiService {
    @GET("mipt_network/chats")
    suspend fun getChats(): GetChatsResponse

    @POST("mipt_network/create_chat")
    suspend fun createChat(@Query("name") name: String): GetChatsResponse

    @POST("mipt_network/msg")
    suspend fun createMessage(@Query("id") chatId: Int, @Query("text") text: String): GetMessagesResponse

    @GET("mipt_network/chat")
    suspend fun getMessages(@Query("id") chatId: Int): GetMessagesResponse
}
