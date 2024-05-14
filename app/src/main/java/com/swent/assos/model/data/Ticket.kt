package com.swent.assos.model.data

data class Ticket(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val status: ParticipationStatus = ParticipationStatus.Participant,
)

enum class ParticipationStatus(name: String) {
  Staff("Staff"),
  Participant("Participant"),
}
