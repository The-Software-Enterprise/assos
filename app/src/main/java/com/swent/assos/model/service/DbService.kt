package com.swent.assos.model.service

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User

interface DbService {
  // Users --------------------------------------------------------------------
  suspend fun getUser(userId: String): User

  suspend fun getUserByEmail(email: String, onSuccess: () -> Unit, onFailure: () -> Unit): User

  // Associations ---------------------------------------------------------------
  suspend fun getAllAssociations(lastDocumentSnapshot: DocumentSnapshot?): List<Association>

  suspend fun getAssociationById(associationId: String): Association

  suspend fun getEventById(eventId: String): Event

  suspend fun applyStaffing(
      eventId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun applyJoinAsso(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  // News ---------------------------------------------------------------------
  suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News>

  suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News>

  suspend fun addApplicant(
      toWhat: String,
      id: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun unAcceptStaff(applicantId: String, eventId: String)

  suspend fun acceptStaff(applicantId: String, eventId: String)

  fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  suspend fun getNews(associationId: String, lastDocumentSnapshot: DocumentSnapshot?): List<News>

  // Events -------------------------------------------------------------------
  suspend fun getEventsFromAnAssociation(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event>

  suspend fun getEventsFromAssociations(
      associationIds: List<String>,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event>

  suspend fun createEvent(event: Event, onSuccess: () -> Unit, onError: (String) -> Unit)

  suspend fun getEventFromId(eventId: String): Event

  // Follow -------------------------------------------------------------------
  suspend fun followAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun unfollowAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun addTicketToUser(
      applicantId: String,
      eventId: String,
      status: ParticipationStatus,
  )

  suspend fun removeTicketFromUser(
      applicantId: String,
      eventId: String,
      status: ParticipationStatus
  )

  suspend fun joinAssociation(
      triple: Triple<String, String, Int>,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun updateBanner(associationId: String, banner: Uri)

  // Tickets ---------------------------------------------------------------

  suspend fun getTicketsUser(userId: String): List<Ticket>

  suspend fun getTickets(userId: String, lastDocumentSnapshot: DocumentSnapshot?): List<Ticket>

  suspend fun getTicketFromId(ticketId: String): Ticket

  suspend fun getTicketsFromEventId(eventId: String): List<Ticket>

  suspend fun getApplicantsByEventId(eventId: String): List<Applicant>

  suspend fun getApplicantsByAssoId(assoId: String): List<Applicant>

  suspend fun acceptApplicant(applicantId: String, assoId: String)

  suspend fun unAcceptApplicant(applicantId: String, assoId: String)

  fun serialize(user: User): Map<String, Any>

  suspend fun addUser(users: User)

  suspend fun quitAssociation(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )
}
