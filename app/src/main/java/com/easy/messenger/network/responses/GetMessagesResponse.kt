package com.easy.messenger.network.responses

import com.easy.messenger.model.entities.MessageEntity

data class GetMessagesResponse(
    val id: Int,
    val messages: List<MessageEntity>,
)
