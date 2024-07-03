package com.example.test.data.stub

import com.example.chat.data.api.ChatApi
import com.example.chat.data.api.model.FetchMessageResponse
import com.example.chat.data.api.model.MessagesResponse
import com.example.chat.data.api.model.SendMessageDto

class ChatApiStub: ChatApi {

    var resultProvider: () -> MessagesResponse = {
        MessagesResponse(
            emptyList()
        )
    }

    override suspend fun sendMessage(params: Map<String, String>): SendMessageDto {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMessage(messageId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun editMessage(messageId: Int, content: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addReaction(idMessage: Int, params: Map<String, String>) {
        TODO("Not yet implemented")
    }

    override suspend fun removeReaction(messageId: Int, emojiName: String) =
        TODO("Not yet implemented")

    override suspend fun getMessages(
        params: Map<String, String>,
        applyMarkdown: Boolean
    ): MessagesResponse = resultProvider()

    override suspend fun fetchMessage(
        messageId: Int,
        applyMarkdown: Boolean
    ): FetchMessageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun changeMessageTopic(messageId: Int, newTopic: String) {
        TODO("Not yet implemented")
    }
}
