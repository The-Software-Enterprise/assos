package com.swent.assos.model.service.impl

import android.content.Context
import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.CommitteeMember
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import com.swent.assos.model.deSerializeOpenPositions
import com.swent.assos.model.deserializeApplicant
import com.swent.assos.model.deserializeAssociation
import com.swent.assos.model.deserializeCommitteeMember
import com.swent.assos.model.deserializeEvent
import com.swent.assos.model.deserializeNews
import com.swent.assos.model.deserializeTicket
import com.swent.assos.model.deserializeUser
import com.swent.assos.model.isNetworkAvailable
import com.swent.assos.model.local_database.LocalDatabaseProvider
import com.swent.assos.model.serialize
import com.swent.assos.model.service.DbService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
import okio.IOException

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : DbService {
  private val _localDatabase = LocalDatabaseProvider.getLocalDatabase(context)

  override suspend fun getUser(userId: String): User {
    val query = firestore.collection("users").document(userId)
    val snapshot = query.get().await() ?: return User()

    return when (snapshot.exists()) {
      false -> User(id = userId)
      true -> deserializeUser(snapshot)
    }
  }

  override suspend fun getPositions(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<OpenPositions> {
    val query = firestore.collection("associations/$associationId/positions")
    val snapshot =
        when (lastDocumentSnapshot) {
          null -> query.limit(10).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    return when (snapshot.isEmpty) {
      true -> emptyList()
      false -> snapshot.documents.map { deSerializeOpenPositions(it) }
    }
  }

  override suspend fun getPositions(associationId: String): List<OpenPositions> {
    val query = firestore.collection("associations/$associationId/positions")
    val snapshot = query.get().await()
    return when (snapshot.isEmpty) {
      true -> emptyList()
      false -> snapshot.documents.map { deSerializeOpenPositions(it) }
    }
  }

  override suspend fun addPosition(associationId: String, openPositions: OpenPositions) {
    firestore
        .collection("associations/$associationId/positions")
        .add(serialize(openPositions))
        .await()
  }

  override suspend fun getPosition(associationId: String, positionId: String): OpenPositions {
    val query = firestore.collection("associations/$associationId/positions").document(positionId)
    val snapshot = query.get().await() ?: return OpenPositions()
    return deSerializeOpenPositions(snapshot)
  }

  override suspend fun deletePosition(
      associationId: String,
      positionId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestore
        .collection("associations/$associationId/positions")
        .document(positionId)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError(it.message ?: "Error") }
  }

  override suspend fun getCommittee(associationId: String): List<CommitteeMember> {
    val members = mutableListOf<CommitteeMember>()
    val querySnapshot = firestore.collection("associations/$associationId/committee").get().await()

    for (document in querySnapshot.documents) {
      members.add(deserializeCommitteeMember(document))
    }

    return members
  }

  override suspend fun addUser(user: User) {
    // get the id of the user
    val id = firestore.collection("users").add(serialize(user)).await().id
    // set the id of the user
    firestore.collection("users").document(id).set(serialize(user.copy(id = id))).await()
    DataCache.currentUser.value = user.copy(id = id)
  }

  override suspend fun getUserByEmail(
      email: String,
      onSuccess: () -> Unit,
      onFailure: () -> Unit
  ): User {
    val query = firestore.collection("users").whereEqualTo("email", email)
    val snapshot = query.get().await()
    return when (snapshot.isEmpty || snapshot.documents.isEmpty()) {
      true -> {
        onFailure()
        User(email = email)
      }
      false -> {
        onSuccess()
        deserializeUser(snapshot.documents[0])
      }
    }
  }

  override suspend fun deleteApplicants(eventId: String) {
    // get all the applicants from the event
    val query = firestore.collection("events/$eventId/applicants")
    val snapshot = query.get().await()

    // delete all the applicants
    for (documents in snapshot.documents) {
      firestore
          .collection("users")
          .document(documents["userId"].toString())
          .update("appliedStaffing", FieldValue.arrayRemove(eventId))
    }
    for (document in snapshot.documents) {
      firestore.collection("events/$eventId/applicants").document(document.id).delete()
    }
  }

  override suspend fun getTicketsUser(userId: String): List<Ticket> {
    // get all the tickets from the user from the ids of tickets in the user collection
    when {
      userId.isEmpty() -> return emptyList()
    }
    val query = firestore.collection("users/$userId/tickets")
    // get all the tickets from the user from the ids of tickets in the user collection
    val snapshot = query.get().await()
    return when (snapshot.isEmpty) {
      true -> emptyList()
      false ->
          snapshot.documents.map { doc ->
            val ticketId = doc.id
            val ticketQuery = firestore.collection("tickets").document(ticketId)
            deserializeTicket(ticketQuery.get().await())
          }
    }
  }

  override suspend fun getNews(newsId: String): News {
    return when (newsId.isEmpty()) {
      true -> News()
      false -> {
        val query = firestore.collection("news").document(newsId)
        val snapshot = query.get().await() ?: return News()
        deserializeNews(snapshot)
      }
    }
  }

  override suspend fun getAllAssociations(
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Association> {

    var query =
        when (lastDocumentSnapshot) {
          null -> firestore.collection("associations").orderBy("acronym")
          else -> firestore.collection("associations").startAfter(lastDocumentSnapshot)
        }
    val localAssociations = _localDatabase.associationDao().getAllAssociations()

    if (!isNetworkAvailable(context)) {
      return localAssociations
    }

    try {
      val snapshot =
          if (localAssociations.isEmpty()) {
            query.get().await()
          } else {
            query.limit(10).get().await()
          }
      val associations = snapshot.documents.map { deserializeAssociation(it) }
      _localDatabase.associationDao().insertAssociation(*associations.toTypedArray())
      return associations
    } catch (e: IOException) {
      return localAssociations
    }
  }

  override suspend fun removeTicketFromUser(
      applicantId: String,
      eventId: String,
      status: ParticipationStatus
  ) {
    // remove ticket from ticket collection
    firestore
        .collection("tickets")
        .whereEqualTo("userId", applicantId)
        .whereEqualTo("eventId", eventId)
        .whereEqualTo("participantStatus", status)
        .get()
        .addOnSuccessListener {
          for (document in it.documents) {
            firestore.collection("tickets").document(document.id).delete()
          }
        }
  }

  override suspend fun deleteEvent(eventId: String) {
    firestore.collection("events").document(eventId).delete().await()
  }

  override suspend fun addTicketToUser(
      applicantId: String,
      eventId: String,
      status: ParticipationStatus,
  ) {
    // add ticket to ticket collection and get the ticket id
    val ticket =
        firestore
            .collection("tickets")
            .add(
                mapOf(
                    "userId" to applicantId,
                    "eventId" to eventId,
                    "participantStatus" to status.name))
            .await()
    val ticketId = ticket?.id ?: return
    // add ticket id to user collection tickets
    firestore
        .collection("users/$applicantId/tickets")
        .document(ticketId)
        .set(mapOf("ticketId" to ticketId))
        .await()
  }

  private fun apply(
      id: String,
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (String) -> Unit,
      event: Boolean
  ) {

    val prefix =
        when (event) {
          true -> "Events"
          false -> "Associations"
        }
    firestore
        .collection("${prefix.lowercase()}/$id/applicants")
        .add(mapOf("userId" to userId, "status" to "pending", "createdAt" to Timestamp.now()))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure("Error") }
    firestore
        .collection("users")
        .document(userId)
        .update("applied$prefix", FieldValue.arrayUnion(id))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure("Error") }
  }

  override suspend fun applyJoinAsso(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    apply(assoId, userId, onSuccess, onError, false)
  }

  override suspend fun applyStaffing(
      eventId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    apply(eventId, userId, onSuccess, onError, true)
  }

  override suspend fun removeJoinApplication(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestore
        .collection("users")
        .document(userId)
        .update("appliedAssociation", FieldValue.arrayRemove(assoId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Error") }

    val querySnapshot = firestore.collection("associations/$assoId/applicants").get().await()

    for (document in querySnapshot.documents) {
      val applicant = deserializeApplicant(document)
      if (applicant.userId == userId) {
        firestore.collection("associations/$assoId/applicants").document(document.id).delete()
      }
    }
  }

  override suspend fun removeStaffingApplication(
      eventId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestore
        .collection("users")
        .document(userId)
        .update("appliedStaffing", FieldValue.arrayRemove(eventId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError("Error") }

    val querySnapshot = firestore.collection("events/$eventId/applicants").get().await()

    for (document in querySnapshot.documents) {
      val applicant = deserializeApplicant(document)
      if (applicant.userId == userId) {
        firestore.collection("events/$eventId/applicants").document(document.id).delete()
      }
    }
  }

  override suspend fun getEventById(eventId: String): Event {
    val query = firestore.collection("events").document(eventId)
    val snapshot = query.get().await() ?: return Event("")
    return deserializeEvent(snapshot)
  }

  override suspend fun getApplicantsByEventId(eventId: String): List<Applicant> {
    val applicants = mutableListOf<Applicant>()
    val querySnapshot = firestore.collection("events/$eventId/applicants").get().await()

    for (document in querySnapshot.documents) {
      applicants.add(deserializeApplicant(document))
    }
    return applicants
  }

  override suspend fun getApplicantsByAssoId(assoId: String): List<Applicant> {
    val applicants = mutableListOf<Applicant>()
    val querySnapshot = firestore.collection("associations/$assoId/applicants").get().await()

    for (document in querySnapshot.documents) {
      applicants.add(deserializeApplicant(document))
    }

    return applicants
  }

  override suspend fun getApplicantByAssoIdAndUserId(assoId: String, userId: String): Applicant {
    val querySnapshot =
        firestore
            .collection("associations/$assoId/applicants")
            .whereEqualTo("userId", userId)
            .get()
            .await()
    val doc = querySnapshot.documents.firstOrNull() ?: return Applicant()
    return deserializeApplicant(doc)
  }

  override suspend fun getApplicantByEventIdAndUserId(eventId: String, userId: String): Applicant {
    val querySnapshot =
        firestore
            .collection("events/$eventId/applicants")
            .whereEqualTo("userId", userId)
            .get()
            .await()
    val doc = querySnapshot.documents.firstOrNull() ?: return Applicant()
    return deserializeApplicant(doc)
  }

  override suspend fun acceptApplicant(applicantId: String, assoId: String) {
    firestore.document("associations/$assoId/applicants/$applicantId").update("status", "accepted")
  }

  override suspend fun unAcceptApplicant(applicantId: String, assoId: String) {
    firestore.document("associations/$assoId/applicants/$applicantId").update("status", "pending")
  }

  override suspend fun rejectApplicant(applicantId: String, assoId: String) {
    firestore.document("associations/$assoId/applicants/$applicantId").update("status", "rejected")
  }

  override suspend fun rejectStaff(applicantId: String, eventId: String) {
    firestore.document("events/$eventId/applicants/$applicantId").update("status", "rejected")
  }

  override suspend fun unAcceptStaff(applicantId: String, eventId: String) {
    firestore.document("events/$eventId/applicants/$applicantId").update("status", "pending")
  }

  override suspend fun acceptStaff(applicantId: String, eventId: String) {
    firestore.document("events/$eventId/applicants/$applicantId").update("status", "accepted")
  }

  override suspend fun getAssociationById(associationId: String): Association {
    val query =
        firestore.document("associations/$associationId").get().await() ?: return Association()
    return deserializeAssociation(query)
  }

  override suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News> {
    val query = firestore.collection("news").orderBy("createdAt", Query.Direction.DESCENDING)
    val snapshot =
        when (lastDocumentSnapshot) {
          null -> query.limit(50).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(50).get().await()
        }
    return snapshot.documents.map { deserializeNews(it) }
  }

  override suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News> {
    val associations =
        DataCache.currentUser.value.following +
            DataCache.currentUser.value.associations.map { it.first }
    val news =
        getAllNews(lastDocumentSnapshot).filter { news ->
          associations.isEmpty() || news.associationId in associations
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

  override suspend fun deleteNews(newsId: String) {
    firestore.collection("news").document(newsId).delete().await()
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
        when (lastDocumentSnapshot) {
          null -> query.limit(10).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(10).get().await()
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
        when (lastDocumentSnapshot) {
          null -> query.limit(10).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    return snapshot.documents.map { deserializeEvent(it) }
  }

  override suspend fun getEventsFromAssociations(
      associationIds: List<String>,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> {
    val query =
        firestore
            .collection("events")
            .whereIn("associationId", associationIds)
            .whereGreaterThan("startTime", Timestamp.now())
            .orderBy("startTime", Query.Direction.ASCENDING)
    val snapshot =
        when (lastDocumentSnapshot) {
          null -> query.limit(10).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(10).get().await()
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
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
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
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
          .update("following", FieldValue.arrayRemove(associationId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Unfollow Error") }
    }
  }

  override suspend fun saveEvent(
      eventId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
          .update("savedEvents", FieldValue.arrayUnion(eventId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Saving Error") }
    }
  }

  override suspend fun saveNews(newsId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
          .update("savedNews", FieldValue.arrayUnion(newsId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Saving Error") }
    }
  }

  override suspend fun unSaveEvent(
      eventId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
          .update("savedEvents", FieldValue.arrayRemove(eventId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("UnSaving Error") }
    }
  }

  override suspend fun unSaveNews(
      newsId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    auth.currentUser?.let {
      firestore
          .collection("users")
          .document(it.uid)
          .update("savedNews", FieldValue.arrayRemove(newsId))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("UnSaving Error") }
    }
  }

  override suspend fun joinAssociation(
      triple: Triple<String, String, Int>,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestore
        .collection("users")
        .document(userId)
        .update(
            "associations",
            FieldValue.arrayUnion(
                mapOf(
                    "assoId" to triple.first, "position" to triple.second, "rank" to triple.third)))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onError(it.message ?: "") }
    firestore
        .collection("associations/${triple.first}/committee")
        .add(mapOf("memberId" to userId, "position" to triple.second, "rank" to triple.third))
  }

  override suspend fun quitAssociation(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    val userRef = firestore.collection("users").document(userId)

    userRef
        .get()
        .addOnSuccessListener { document ->
          if (document.exists()) {
            val associations = document["associations"] as? List<Map<String, Any>>
            associations
                ?.find { it["assoId"] == assoId }
                ?.let { associationToQuit ->
                  userRef
                      .update("associations", FieldValue.arrayRemove(associationToQuit))
                      .addOnSuccessListener { onSuccess() }
                      .addOnFailureListener {
                        onError(it.message ?: "Failed to remove association")
                      }
                } ?: onError("Association not found")
          } else {
            onError("User not found")
          }
        }
        .addOnFailureListener { onError(it.message ?: "Error fetching user details") }

    val querySnapshot = firestore.collection("associations/$assoId/committee").get().await()

    for (document in querySnapshot.documents) {
      val member = deserializeCommitteeMember(document)
      if (member.memberId == userId) {
        firestore.collection("associations/$assoId/committee").document(document.id).delete()
      }
    }
  }

  override suspend fun updateBanner(associationId: String, banner: Uri) {
    firestore
        .collection("associations")
        .document(associationId)
        .update("banner", banner.toString())
        .await()
  }

  override suspend fun getTicketsFromEventId(eventId: String): List<Ticket> {
    val query = firestore.collection("tickets").whereEqualTo("eventId", eventId)
    val snapshot = query.get().await()
    return snapshot.documents.map { deserializeTicket(it) }
  }
}
