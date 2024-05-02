package com.swent.assos.model.service

import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User

interface DbService {
  // Users --------------------------------------------------------------------
  suspend fun getUser(userId: String): User

  // Associations ---------------------------------------------------------------
  suspend fun getAllAssociations(lastDocumentSnapshot: DocumentSnapshot?): List<Association>

  suspend fun getAssociationById(associationId: String): Association

  // News ---------------------------------------------------------------------
  suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News>

  suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News>

  fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  fun updateNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  fun deleteNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  suspend fun getNews(associationId: String, lastDocumentSnapshot: DocumentSnapshot?): List<News>

  // Events -------------------------------------------------------------------
  suspend fun getAllEvents(lastDocumentSnapshot: DocumentSnapshot?): List<Event>

  suspend fun getEventsFromAnAssociation(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event>

  suspend fun getEventsFromAssociations(
      associationIds: List<String>,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event>

  suspend fun createEvent(event: Event, onSuccess: () -> Unit, onError: (String) -> Unit)

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
}
