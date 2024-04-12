package com.swent.assos.model.service

import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.News

interface DbService {
  // Associations ---------------------------------------------------------------
  suspend fun getAllAssociations(lastDocumentSnapshot: DocumentSnapshot?): List<Association>

  suspend fun getAssociationById(associationId: String): Association

  // News ---------------------------------------------------------------------
  suspend fun getAllNews(): List<News>

  fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  fun updateNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)

  fun deleteNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit)
}
