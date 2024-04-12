package com.swent.assos.model.service

import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News

interface DbService {
  suspend fun getAllAssociations(lastDocumentSnapshot: DocumentSnapshot?): List<Association>

  suspend fun getAllNews(): List<News>

  suspend fun getAllEvents(): List<Event>

  suspend fun getAssociationById(associationId: String): Association
}
