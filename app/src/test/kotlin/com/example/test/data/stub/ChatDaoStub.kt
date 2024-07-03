package com.example.test.data.stub

import com.example.chat.data.database.ChatDao
import com.example.chat.data.database.model.MessageEntity
import com.example.chat.data.database.model.MessageWithReactions
import com.example.chat.data.database.model.ReactionEntity
import com.example.common.core.TopicId

class ChatDaoStub: ChatDao {

    var resultProvider: () -> List<MessageWithReactions> = { emptyList() }

    override suspend fun getMessages(topicId: TopicId): List<MessageWithReactions> {
        return resultProvider()
    }

    override suspend fun clearMessages() =
        TODO("Not yet implemented")

    override suspend fun saveMessages(messages: List<MessageEntity>) =
        TODO("Not yet implemented")

    override suspend fun clearReactions() =
        TODO("Not yet implemented")

    override suspend fun saveReactions(reactions: List<ReactionEntity>) =
        TODO("Not yet implemented")
}
