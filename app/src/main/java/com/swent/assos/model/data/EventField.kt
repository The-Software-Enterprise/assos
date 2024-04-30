package com.swent.assos.model.data

open class EventField(open var title: String = "", open val type: EventFieldType)

data class EventFieldImage(
    override var title: String,
    val image: String,
    override val type: EventFieldType = EventFieldType.IMAGE
) : EventField(type = type)

data class EventFieldText(
    override var title: String,
    val text: String,
    override val type: EventFieldType = EventFieldType.TEXT
) : EventField(type = type)

enum class EventFieldType {
  IMAGE,
  TEXT
}
