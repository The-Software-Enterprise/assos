package com.swent.assos.model.service

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.CommitteeMember
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.OpenPositions
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

  suspend fun removeJoinApplication(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun removeStaffingApplication(
      eventId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  // News ---------------------------------------------------------------------
  suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News>

  suspend fun deleteNews(newsId: String)

  suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News>

  suspend fun unAcceptStaff(applicantId: String, eventId: String)

  suspend fun rejectApplicant(applicantId: String, assoId: String)

  suspend fun rejectStaff(applicantId: String, eventId: String)

  suspend fun acceptStaff(applicantId: String, eventId: String)

  suspend fun deleteApplicants(eventId: String)

  fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  suspend fun getNews(associationId: String, lastDocumentSnapshot: DocumentSnapshot?): List<News>

  suspend fun getNews(newsId: String): News

  suspend fun saveNews(newsId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

  suspend fun unSaveNews(newsId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

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

  suspend fun deleteEvent(eventId: String)

  suspend fun saveEvent(eventId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

  suspend fun unSaveEvent(eventId: String, onSuccess: () -> Unit, onError: (String) -> Unit)

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

  suspend fun getTicketsFromUserIdAndEventId(userId: String, eventId: String): List<Ticket>

  suspend fun getTicketFromId(ticketId: String): Ticket

  suspend fun getTicketsFromEventId(eventId: String): List<Ticket>

  suspend fun getApplicantsByEventId(eventId: String): List<Applicant>

  suspend fun getApplicantsByAssoId(assoId: String): List<Applicant>

  suspend fun getApplicantByAssoIdAndUserId(assoId: String, userId: String): Applicant

  suspend fun getApplicantByEventIdAndUserId(eventId: String, userId: String): Applicant

  suspend fun acceptApplicant(applicantId: String, assoId: String)

  suspend fun unAcceptApplicant(applicantId: String, assoId: String)

  suspend fun getPositions(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<OpenPositions>

  suspend fun getPositions(associationId: String): List<OpenPositions>

  suspend fun addPosition(associationId: String, openPositions: OpenPositions)

  suspend fun addUser(users: User)

  suspend fun quitAssociation(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun getPosition(associationId: String, positionId: String): OpenPositions

  suspend fun deletePosition(
      associationId: String,
      positionId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun getCommittee(associationId: String): List<CommitteeMember>
}
