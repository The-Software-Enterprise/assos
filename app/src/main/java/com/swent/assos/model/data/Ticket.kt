package com.swent.assos.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class Ticket(
    @PrimaryKey val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val status: ParticipationStatus = ParticipationStatus.Participant,
)

enum class ParticipationStatus(name: String) {
  Staff("Staff"),
  Participant("Participant"),
}
