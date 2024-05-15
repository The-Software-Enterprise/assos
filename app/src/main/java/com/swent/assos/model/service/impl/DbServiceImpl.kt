package com.swent.assos.model.service.impl

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import com.swent.assos.model.localDateTimeToTimestamp
import com.swent.assos.model.service.DbService
import com.swent.assos.model.timestampToLocalDateTime
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
        firstName = snapshot.getString("firstname") ?: "",
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

  override suspend fun addTicketToUser(
      email: String,
      eventId: String,
      onSuccess: () -> Unit,
      onFailure: () -> Unit
  ) {

    val user = firestore.collection("users").whereEqualTo("email", email).get().await()
    when {
      // if the user does not exist
      user.documents.isEmpty() -> {
        onFailure()
        return
      }
    }
    val userId = user.documents[0].id
    val ticket = mapOf("eventId" to eventId, "userId" to userId)
    // add the ticket to the ticket collection and get the id created
    val ticketId = firestore.collection("tickets").add(ticket).await().id
    // and add the ticket to the ticket collection in user
    firestore
        .collection("users")
        .document(userId)
        .collection("tickets")
        .document(ticketId)
        .set(ticket)
    onSuccess()
  }

  override suspend fun applyStaffing(
      eventId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {

    this.addApplicant("events", eventId, userId, onSuccess, onError)
  }

  override suspend fun applyJoinAsso(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    this.addApplicant("associations", assoId, userId, onSuccess, onError)
  }

  override suspend fun getEventById(eventId: String): Event {
    val query = firestore.collection("events").document(eventId)
    val snapshot = query.get().await() ?: return Event("")
    return deserializeEvent(snapshot)
  }

  override suspend fun getApplicantsByEventId(eventId: String): List<Applicant> {
    val applicants = mutableListOf<Applicant>()
    val querySnapshot =
        firestore.collection("events").document(eventId).collection("applicants").get().await()

    for (document in querySnapshot.documents) {
      applicants.add(deserializeApplicant(document))
    }

    return applicants
  }

  override suspend fun getApplicantsByAssoId(assoId: String): List<Applicant> {
    val applicants = mutableListOf<Applicant>()
    val querySnapshot =
        firestore.collection("associations").document(assoId).collection("applicants").get().await()

    for (document in querySnapshot.documents) {
      applicants.add(deserializeApplicant(document))
    }

    return applicants
  }

  override suspend fun unAcceptStaff(applicantId: String, eventId: String) {

    firestore
        .collection("events")
        .document(eventId)
        .collection("applicants")
        .document(applicantId)
        .update("status", "pending")
  }

  override suspend fun acceptStaff(applicantId: String, eventId: String) {

    firestore
        .collection("events")
        .document(eventId)
        .collection("applicants")
        .document(applicantId)
        .update("status", "accepted")
  }

  override suspend fun addApplicant(
      toWhat: String,
      id: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser
    if (user != null) {
      firestore
          .collection(toWhat)
          .document(id)
          .collection("applicants")
          .add(mapOf("userId" to userId, "status" to "pending", "createdAt" to Timestamp.now()))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError(it.message ?: "") }
    }
  }

  override suspend fun getAssociationById(associationId: String): Association {
    val query = firestore.collection("associations").document(associationId)
    val snapshot = query.get().await() ?: return Association()
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

  override suspend fun getEventFromId(eventId: String): Event {
    val query = firestore.collection("events").document(eventId)
    val snapshot = query.get().await() ?: return Event("", "", "")
    return deserializeEvent(snapshot)
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

  override suspend fun joinAssociation(
      triple: Triple<String, String, Int>,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val user = auth.currentUser
    if (user != null) {
      firestore
          .collection("users")
          .document(user.uid)
          .update(
              "associations",
              FieldValue.arrayUnion(
                  mapOf(
                      "assoId" to triple.first,
                      "position" to triple.second,
                      "rank" to triple.third)))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError(it.message ?: "") }
    }
  }

  override suspend fun updateBanner(associationId: String, banner: Uri) {
    firestore
        .collection("associations")
        .document(associationId)
        .update("banner", banner.toString())
        .await()
  }

  override suspend fun getTickets(
      userId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Ticket> {
    if (userId.isEmpty()) {
      return emptyList()
    }
    val query = firestore.collection("tickets").whereEqualTo("userId", userId)
    val snapshot =
        if (lastDocumentSnapshot == null) {
          query.limit(10).get().await()
        } else {
          query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    if (snapshot.isEmpty) {
      return emptyList()
    }
    return snapshot.documents.map { deserializeTicket(it) }
  }

  override suspend fun getTicketFromId(ticketId: String): Ticket {
    val query = firestore.collection("tickets").document(ticketId)
    val snapshot = query.get().await() ?: return Ticket("", "", "")
    return deserializeTicket(snapshot)
  }
}

fun serialize(event: Event): Map<String, Any> {
  return mapOf(
      "title" to event.title,
      "description" to event.description,
      "associationId" to event.associationId,
      "image" to event.image.toString(),
      "startTime" to localDateTimeToTimestamp(event.startTime),
      "endTime" to localDateTimeToTimestamp(event.endTime),
      "fields" to
          event.fields.map {
            when (it) {
              is Event.Field.Text -> mapOf("type" to "text", "title" to it.title, "text" to it.text)
              is Event.Field.Image ->
                  mapOf("type" to "image", "uris" to it.uris.map { uri -> uri.toString() })
            }
          })
}

fun deserializeEvent(doc: DocumentSnapshot): Event {
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
                .mapNotNull {
                  if (it is Map<*, *>) {
                    val type = it["type"] as? String
                    when (type) {
                      "text" -> {
                        val title = it["title"] as? String ?: ""
                        val text = it["text"] as? String ?: ""
                        Event.Field.Text(title, text)
                      }
                      "image" -> {
                        val uris =
                            (it["uris"] as? List<*>)?.filterIsInstance<String>()?.map {
                              Uri.parse(it)
                            }
                        if (uris != null) {
                          Event.Field.Image(uris)
                        } else {
                          null
                        }
                      }
                      else -> null
                    }
                  } else {
                    null
                  }
                }
                .filterNotNull()
          } else {
            emptyList()
          },
      documentSnapshot = doc)
}

private fun deserializeTicket(doc: DocumentSnapshot): Ticket {
  return Ticket(
      id = doc.id, eventId = doc.getString("eventId") ?: "", userId = doc.getString("userId") ?: "")
}

fun serialize(news: News): Map<String, Any> {
  return mapOf(
      "title" to news.title,
      "description" to news.description,
      "createdAt" to localDateTimeToTimestamp(news.createdAt),
      "associationId" to news.associationId,
      "images" to news.images,
      "eventIds" to news.eventIds)
}

fun deserializeNews(doc: DocumentSnapshot): News {
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

fun deserializeAssociation(doc: DocumentSnapshot): Association {
  return Association(
      id = doc.id,
      acronym = doc.getString("acronym") ?: "",
      fullname = doc.getString("fullname") ?: "",
      url = doc.getString("url") ?: "",
      description = doc.getString("description") ?: "",
      logo = doc.getString("logo")?.let { url -> Uri.parse(url) } ?: Uri.EMPTY,
      banner = doc.getString("banner")?.let { url -> Uri.parse(url) } ?: Uri.EMPTY,
      documentSnapshot = doc)
}

private fun deserializeApplicant(doc: DocumentSnapshot): Applicant {
  return Applicant(
      id = doc.id,
      userId = doc.getString("userId") ?: "",
      status = doc.getString("status") ?: "unknown",
      createdAt = timestampToLocalDateTime(doc.getTimestamp("createdAt")))
}
