package com.swent.assos.model.service.impl

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthService,
) : DbService {

  override suspend fun getUser(userId: String): User {
    val query = firestore.collection("users").document(userId)
    val snapshot = query.get().await() ?: return User()
    return User(
        id = snapshot.id,
        firstName = snapshot.getString("firstname") ?: "",
        lastName = snapshot.getString("name") ?: "",
        email = snapshot.getString("email") ?: "",
        following =
            if (snapshot.get("following") is MutableList<*>) {
              (snapshot.get("following") as MutableList<*>)
                  .filterIsInstance<String>()
                  .toMutableList()
            } else {
              mutableListOf()
            },
        associations =
            if (snapshot.get("associations") is List<*>) {
              (snapshot.get("associations") as List<*>)
                  .filterIsInstance<Map<String, Any>>()
                  .map {
                    Triple(
                        it["assoId"] as String,
                        it["position"] as String,
                        (it["rank"] as Long).toInt())
                  }
                  .toList()
            } else {
              emptyList()
            })
  }

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

  override suspend fun getNews(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<News> {
    val query =
        firestore
            .collection("news")
            .whereEqualTo("associationId", associationId)
            .orderBy("date", Query.Direction.DESCENDING)
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
          eventId = it.getString("eventId") ?: "",
          documentSnapshot = it)
    }
  }

  override suspend fun getAllEvents(lastDocumentSnapshot: DocumentSnapshot?): List<Event> {
    val query = firestore.collection("events").orderBy("date", Query.Direction.ASCENDING)
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
      Event(
          id = it.id,
          title = it.getString("title") ?: "",
          description = it.getString("description") ?: "",
          date = it.getString("date") ?: "",
          associationId = it.getString("associationId") ?: "",
          image = it.getString("image") ?: "",
          startTime = timestampToLocalDateTime(it.getTimestamp("startTime")),
          endTime = timestampToLocalDateTime(it.getTimestamp("endTime")),
          documentSnapshot = it)
    }
  }

  override suspend fun getAllEventsFromAnAssociation(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> {
    val query =
        firestore
            .collection("events")
            .whereEqualTo("associationId", associationId)
            .orderBy("date", Query.Direction.ASCENDING)
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
      Event(
          id = it.id,
          title = it.getString("title") ?: "",
          description = it.getString("description") ?: "",
          date = it.getString("date") ?: "",
          associationId = it.getString("associationId") ?: "",
          image = it.getString("image") ?: "",
          startTime = timestampToLocalDateTime(it.getTimestamp("startTime")),
          endTime = timestampToLocalDateTime(it.getTimestamp("endTime")),
          documentSnapshot = it)
    }
  }

  override suspend fun getEvents(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> {
    val query =
        firestore
            .collection("events")
            .whereEqualTo("associationId", associationId)
            .whereGreaterThan("date", LocalDate.now())
            .orderBy("date", Query.Direction.ASCENDING)
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
      Event(
          id = it.id,
          title = it.getString("title") ?: "",
          description = it.getString("description") ?: "",
          date = it.getString("date") ?: "",
          associationId = it.getString("associationId") ?: "",
          image = it.getString("image") ?: "",
          startTime = timestampToLocalDateTime(it.getTimestamp("startTime")),
          endTime = timestampToLocalDateTime(it.getTimestamp("endTime")),
          documentSnapshot = it)
    }
  }

  override suspend fun createEvent(
      associationId: String,
      event: Event,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestore
        .collection("events")
        .add(
            mapOf(
                "title" to event.title,
                "description" to event.description,
                "date" to event.date,
                "associationId" to associationId,
                "image" to event.image,
                "startTime" to event.startTime,
                "endTime" to event.endTime))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Error") }
  }

  override suspend fun followAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser.first()
    firestore
        .collection("users")
        .document(user.uid)
        .update("following", FieldValue.arrayUnion(associationId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Error") }
  }

  override suspend fun unfollowAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser.first()
    firestore
        .collection("users")
        .document(user.uid)
        .update("following", FieldValue.arrayRemove(associationId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Unfollow Error") }
  }

  private fun timestampToLocalDateTime(timestamp: Timestamp?): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp?.seconds ?: 0), ZoneId.systemDefault())
  }
}
