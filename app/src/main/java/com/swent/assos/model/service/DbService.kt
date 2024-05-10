package com.swent.assos.model.service

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User

interface DbService {
  // Users --------------------------------------------------------------------
  suspend fun getUser(userId: String): User

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

  suspend fun joinAssociation(
      triple: Triple<String, String, Int>,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  )

  suspend fun updateBanner(associationId: String, banner: Uri)

  // Tickets ---------------------------------------------------------------

  suspend fun getTickets(userId: String, lastDocumentSnapshot: DocumentSnapshot?): List<Ticket>

  suspend fun getTicketFromId(ticketId: String): Ticket
}
