package com.swent.assos.model.service.impl

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthService,
) : DbService {

  override suspend fun getAllAssociations(
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Association> {
    val query = firestore.collection("associations").orderBy("acronym")
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map {
      Association(
          id = it.id,
          acronym = it.getString("acronym") ?: "",
          fullname = it.getString("fullname") ?: "",
          url = it.getString("url") ?: "",
          documentSnapshot = it,
          description = it.getString("description") ?: "")
    }
  }

  override suspend fun getAssociationById(associationId: String): Association {
    val query = firestore.collection("associations").document(associationId)
    val snapshot = query.get().await() ?: return Association("", "", "", "", description = "")
    return Association(
        id = snapshot.id,
        acronym = snapshot.getString("acronym") ?: "",
        fullname = snapshot.getString("fullname") ?: "",
        url = snapshot.getString("url") ?: "",
        documentSnapshot = snapshot,
        description = snapshot.getString("description") ?: "")
  }

  override suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News> {
    val query = firestore.collection("news").orderBy("date", Query.Direction.DESCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map {
      News(
          id = it.id,
          title = it.getString("title") ?: "",
          description = it.getString("description") ?: "",
          date = it.getDate("date") ?: Date(),
          associationId = it.getString("associationId") ?: "",
          image = it.getString("image") ?: "",
          eventId = it.getString("eventId") ?: "")
    }
  }

  override suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News> {
    val query = firestore.collection("users").document(userId)
    val snapshot = query.get().await() ?: return emptyList()
    val followedAssociations: List<Association> = snapshot.get("following") as List<Association>
    val associationsTheUserBelongsTo: List<Association> =
        snapshot.get("associations") as List<Association>
    val news =
        getAllNews(lastDocumentSnapshot).filter { news ->
          news.associationId in followedAssociations.map { association -> association.id } ||
              news.associationId in
                  associationsTheUserBelongsTo.map { association -> association.id }
        }
    return news
  }

  override fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit) {
    firestore
        .collection("news")
        .add(
            mapOf(
                "title" to news.title,
                "description" to news.description,
                "date" to Date(),
                "associationId" to news.associationId,
                "image" to news.image,
                "eventId" to news.eventId))
        .addOnSuccessListener { onSucess() }
        .addOnFailureListener { onError(it.message ?: "Error") }
  }

  override fun updateNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit) {
    firestore
        .collection("news")
        .document(news.id)
        .set(
            mapOf(
                "title" to news.title,
                "description" to news.description,
                "image" to news.image,
            ))
        .addOnSuccessListener { onSucess() }
        .addOnFailureListener { onError(it.message ?: "Error") }
  }

  override fun deleteNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit) {
    firestore
        .collection("news")
        .document(news.id)
        .delete()
        .addOnSuccessListener { onSucess() }
        .addOnFailureListener { onError(it.message ?: "Error") }
  }

  override suspend fun getAllEvents(): List<Event> {
    return listOf<Event>(
        Event(
            title = "Distribution de crepes",
            association =
                Association(
                    acronym = "JE EPFL",
                    fullname = "JE EPFL",
                    url = "jeepfl.ch",
                    documentSnapshot = null,
                    description = "asso très cool"),
            date = "01/04/2024",
            description = "yepa des crepes",
            image = ""),
        Event(
            title = "Vin chaud",
            association =
                Association(
                    acronym = "Agepoly",
                    fullname = "Agepoly",
                    url = "agepoly.ch",
                    documentSnapshot = null,
                    description = "asso mere"),
            date = "15/04/2024",
            description = "yepa du vin",
            image = ""))
  }
}
