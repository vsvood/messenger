package com.easy.messenger.network.responses

import com.easy.messenger.model.entities.ChatEntity

data class GetChatsResponse(
    val chats: List<ChatEntity>,
)
