package com.example.chat.data.api.events

import com.example.chat.domain.MessageEvent
import com.example.chat.domain.MessageEventType
import com.example.common.data.events.model.EventDto

internal fun EventDto.toMessageEvent() = MessageEvent(
    id = id,
    type = MessageEventType.valueOfOrOther(type),
)
