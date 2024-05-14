package com.swent.assos.model.data

import com.google.firebase.installations.FirebaseInstallationsException.Status

data class Ticket(
    val id: String = "",
    val eventId: String = "",
    val userId: String = "",
    val status: ParticipationStatus = ParticipationStatus.Participant,
)

enum class ParticipationStatus(str: String) {
    Staff("Staff"),
    Participant("Participant"),
}
