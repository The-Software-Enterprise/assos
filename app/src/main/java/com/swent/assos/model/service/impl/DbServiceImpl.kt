package com.swent.assos.model.service.impl

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.EventFieldImage
import com.swent.assos.model.data.EventFieldText
import com.swent.assos.model.data.EventFieldType
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.localDateTimeToTimestamp
import com.swent.assos.model.service.DbService
import com.swent.assos.model.timestampToLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : DbService {

  override suspend fun getUser(userId: String): User {
    val query = firestore.collection("users").document(userId)
    val snapshot = query.get().await() ?: return User()
    if (!snapshot.exists()) {
      return User()
    }
    return User(
        id = snapshot.id,
        firstName = snapshot.getString("firstname") ?: "", // Handle nullability explicitly
        lastName = snapshot.getString("name") ?: "",
        email = snapshot.getString("email") ?: "",
        following = (snapshot.get("following") as? MutableList<String>) ?: mutableListOf(),
        associations =
            snapshot.get("associations")?.let { associations ->
              (associations as? List<Map<String, Any>>)?.mapNotNull {
                val assoId = it["assoId"] as? String
                val position = it["position"] as? String
                val rank = (it["rank"] as? Long)?.toInt()
                if (assoId != null && position != null && rank != null) {
                  Triple(assoId, position, rank)
                } else {
                  null
                }
              } ?: emptyList()
            } ?: emptyList())
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
    return snapshot.documents.map { deserializeAssociation(it) }
  }

  override suspend fun getAssociationById(associationId: String): Association {
    val query = firestore.collection("associations").document(associationId)
    val snapshot = query.get().await() ?: return Association("", "", "", "", description = "")
    return deserializeAssociation(snapshot)
  }

  override suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News> {
    val query = firestore.collection("news").orderBy("createdAt", Query.Direction.DESCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeNews(it) }
  }

  override suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News> {
    val query = firestore.collection("users").document(userId)
    val snapshot = query.get().await() ?: return emptyList()
    val followedAssociations: List<String> =
        if (snapshot.get("following") is List<*>) {
          (snapshot.get("following") as List<*>).filterIsInstance<String>().toMutableList()
        } else {
          emptyList()
        }
    val associationsTheUserBelongsTo: List<String> =
        if (snapshot.get("associations") is List<*>) {
          (snapshot.get("associations") as List<*>).filterIsInstance<String>().toMutableList()
        } else {
          emptyList()
        }
    if (followedAssociations.isEmpty() && associationsTheUserBelongsTo.isEmpty()) {
      return getAllNews(lastDocumentSnapshot)
    }
    val news =
        getAllNews(lastDocumentSnapshot).filter { news ->
          news.associationId in followedAssociations ||
              news.associationId in associationsTheUserBelongsTo
        }
    return news
  }

  override fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit) {
    firestore
        .collection("news")
        .document(news.id)
        .set(serialize(news))
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
                "images" to news.images,
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
            .orderBy("createdAt", Query.Direction.DESCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeNews(it) }
  }

  override suspend fun getNewsById(newsId: String): News {
    val query = firestore.collection("news").document(newsId)
    val snapshot = query.get().await()
    return News(
        id = snapshot.id,
        title = snapshot.getString("title") ?: "",
        description = snapshot.getString("description") ?: "",
        createdAt = timestampToLocalDateTime(snapshot.getTimestamp("createdAt")),
        associationId = snapshot.getString("associationId") ?: "",
        images =
            if (snapshot.get("images") is List<*>) {
              (snapshot.get("images") as List<*>).filterIsInstance<Uri>().toMutableList()
            } else {
              mutableListOf()
            },
        eventIds =
            if (snapshot.get("eventIds") is List<*>) {
              (snapshot.get("eventIds") as List<*>).filterIsInstance<String>().toMutableList()
            } else {
              mutableListOf()
            },
        documentSnapshot = snapshot)
  }

  override suspend fun getAllEvents(lastDocumentSnapshot: DocumentSnapshot?): List<Event> {
    val query = firestore.collection("events").orderBy("startTime", Query.Direction.ASCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeEvent(it) }
  }

  override suspend fun getEventsFromAnAssociation(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> {
    val query =
        firestore
            .collection("events")
            .whereEqualTo("associationId", associationId)
            .whereGreaterThan("startTime", Timestamp.now())
            .orderBy("startTime", Query.Direction.ASCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeEvent(it) }
  }

  override suspend fun getEventsFromAssociations(
      associationIds: List<String>,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> {
    if (associationIds.isEmpty()) {
      return emptyList()
    }
    val query =
        firestore
            .collection("events")
            .whereIn("associationId", associationIds)
            .whereGreaterThan("startTime", Timestamp.now())
            .orderBy("startTime", Query.Direction.ASCENDING)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeEvent(it) }
  }

  override suspend fun createEvent(event: Event, onSuccess: () -> Unit, onError: (String) -> Unit) {
    firestore
        .collection("events")
        .document(event.id)
        .set(serialize(event))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Error") }
  }

  override suspend fun followAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser
    if (user != null) {
      firestore
          .collection("users")
          .document(user.uid)
          .update("following", FieldValue.arrayUnion(associationId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Error") }
    }
  }

  override suspend fun unfollowAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser
    if (user != null) {
      firestore
          .collection("users")
          .document(user.uid)
          .update("following", FieldValue.arrayRemove(associationId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Unfollow Error") }
    }
  }
}

private fun serialize(event: Event): Map<String, Any> {
  return mapOf(
      "title" to event.title,
      "description" to event.description,
      "associationId" to event.associationId,
      "image" to event.image.toString(),
      "startTime" to localDateTimeToTimestamp(event.startTime ?: LocalDateTime.now()),
      "endTime" to localDateTimeToTimestamp(event.endTime ?: LocalDateTime.now()),
      "fields" to
          event.fields.map {
            when (it.type) {
              EventFieldType.IMAGE ->
                  mapOf(
                      "title" to it.title,
                      "type" to it.type.toString(),
                      "value" to (it as EventFieldImage).image)
              EventFieldType.TEXT ->
                  mapOf(
                      "title" to it.title,
                      "type" to it.type.toString(),
                      "value" to (it as EventFieldText).text)
            }
          })
}

private fun deserializeEvent(doc: DocumentSnapshot): Event {
  return Event(
      id = doc.id,
      title = doc.getString("title") ?: "",
      description = doc.getString("description") ?: "",
      associationId = doc.getString("associationId") ?: "",
      image = Uri.parse(doc.getString("image") ?: ""),
      startTime = timestampToLocalDateTime(doc.getTimestamp("startTime")),
      endTime = timestampToLocalDateTime(doc.getTimestamp("endTime")),
      fields =
          if (doc["fields"] is List<*>) {
            (doc["fields"] as List<*>)
                .filterIsInstance<Map<String, String>>()
                .map { map ->
                  when (map["type"]) {
                    EventFieldType.IMAGE.toString() ->
                        EventFieldImage(title = map["title"] ?: "", image = map["value"] ?: "")
                    EventFieldType.TEXT.toString() ->
                        EventFieldText(title = map["title"] ?: "", text = map["value"] ?: "")
                    else -> EventFieldText("", "")
                  }
                }
                .toMutableList()
          } else {
            mutableListOf()
          },
      documentSnapshot = doc)
}

private fun serialize(news: News): Map<String, Any> {
  return mapOf(
      "title" to news.title,
      "description" to news.description,
      "createdAt" to localDateTimeToTimestamp(news.createdAt),
      "associationId" to news.associationId,
      "images" to news.images,
      "eventIds" to news.eventIds)
}

private fun deserializeNews(doc: DocumentSnapshot): News {
  return News(
      id = doc.id,
      title = doc.getString("title") ?: "",
      description = doc.getString("description") ?: "",
      createdAt = timestampToLocalDateTime(doc.getTimestamp("createdAt")),
      associationId = doc.getString("associationId") ?: "",
      images =
          if (doc["images"] is List<*>) {
            (doc["images"] as List<*>).filterIsInstance<String>().toList().map { Uri.parse(it) }
          } else {
            listOf()
          },
      eventIds =
          if (doc["eventIds"] is List<*>) {
            (doc["eventIds"] as List<*>).filterIsInstance<String>().toMutableList()
          } else {
            mutableListOf()
          },
      documentSnapshot = doc)
}

private fun deserializeAssociation(doc: DocumentSnapshot): Association {
  return Association(
      id = doc.id,
      acronym = doc.getString("acronym") ?: "",
      fullname = doc.getString("fullname") ?: "",
      url = doc.getString("url") ?: "",
      description = doc.getString("description") ?: "",
      logo = doc.getString("logo")?.let { url -> Uri.parse(url) } ?: Uri.EMPTY,
      documentSnapshot = doc)
}
