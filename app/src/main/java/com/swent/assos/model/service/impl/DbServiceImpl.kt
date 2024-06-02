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
import com.swent.assos.model.firestoreCallWithCatchError
import com.swent.assos.model.isNetworkAvailable
import com.swent.assos.model.local_database.LocalDatabaseProvider
import com.swent.assos.model.serialize
import com.swent.assos.model.service.DbService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okio.IOException

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : OpenDbServiceImpl(firestore, auth, context) {

  override suspend fun getEventById(eventId: String): Event {
    return firestoreCallWithCatchError {
      val query = firestore.collection("events").document(eventId)
      val snapshot = query.get().await() ?: return@firestoreCallWithCatchError Event("")
      return@firestoreCallWithCatchError deserializeEvent(snapshot)
    } ?: Event("")
  }

  override suspend fun getApplicantsByEventId(eventId: String): List<Applicant> =
      firestoreCallWithCatchError {
        val applicants = mutableListOf<Applicant>()
        val querySnapshot = firestore.collection("events/$eventId/applicants").get().await()

        for (document in querySnapshot.documents) {
          applicants.add(deserializeApplicant(document))
        }
        return@firestoreCallWithCatchError applicants
      } ?: emptyList()

  override suspend fun getApplicantsByAssoId(assoId: String): List<Applicant> =
      firestoreCallWithCatchError {
        val applicants = mutableListOf<Applicant>()
        val querySnapshot = firestore.collection("associations/$assoId/applicants").get().await()

        for (document in querySnapshot.documents) {
          applicants.add(deserializeApplicant(document))
        }

        return@firestoreCallWithCatchError applicants
      } ?: emptyList()

  override suspend fun getApplicantByAssoIdAndUserId(assoId: String, userId: String): Applicant =
      firestoreCallWithCatchError {
        val querySnapshot =
            firestore
                .collection("associations/$assoId/applicants")
                .whereEqualTo("userId", userId)
                .get()
                .await()
        val doc =
            querySnapshot.documents.firstOrNull() ?: return@firestoreCallWithCatchError Applicant()
        return@firestoreCallWithCatchError deserializeApplicant(doc)
      } ?: Applicant()

  override suspend fun getApplicantByEventIdAndUserId(eventId: String, userId: String): Applicant =
      firestoreCallWithCatchError {
        val querySnapshot =
            firestore
                .collection("events/$eventId/applicants")
                .whereEqualTo("userId", userId)
                .get()
                .await()
        val doc =
            querySnapshot.documents.firstOrNull() ?: return@firestoreCallWithCatchError Applicant()
        return@firestoreCallWithCatchError deserializeApplicant(doc)
      } ?: Applicant()

  override suspend fun acceptApplicant(applicantId: String, assoId: String) {
    firestoreCallWithCatchError {
      firestore
          .document("associations/$assoId/applicants/$applicantId")
          .update("status", "accepted")
    }
  }

  override suspend fun unAcceptApplicant(applicantId: String, assoId: String) {
    firestoreCallWithCatchError {
      firestore.document("associations/$assoId/applicants/$applicantId").update("status", "pending")
    }
  }

  override suspend fun rejectApplicant(applicantId: String, assoId: String) {
    firestoreCallWithCatchError {
      firestore
          .document("associations/$assoId/applicants/$applicantId")
          .update("status", "rejected")
    }
  }

  override suspend fun rejectStaff(applicantId: String, eventId: String) {
    firestoreCallWithCatchError {
      firestore.document("events/$eventId/applicants/$applicantId").update("status", "rejected")
    }
  }

  override suspend fun unAcceptStaff(applicantId: String, eventId: String) {
    firestoreCallWithCatchError {
      firestore.document("events/$eventId/applicants/$applicantId").update("status", "pending")
    }
  }

  override suspend fun acceptStaff(applicantId: String, eventId: String) {
    firestoreCallWithCatchError {
      firestore.document("events/$eventId/applicants/$applicantId").update("status", "accepted")
    }
  }

  override suspend fun getAssociationById(associationId: String): Association =
      firestoreCallWithCatchError {
        val query =
            firestore.document("associations/$associationId").get().await()
                ?: return@firestoreCallWithCatchError Association()
        return@firestoreCallWithCatchError deserializeAssociation(query)
      } ?: Association()

  override suspend fun getAllNews(lastDocumentSnapshot: DocumentSnapshot?): List<News> =
      firestoreCallWithCatchError {
        val query = firestore.collection("news").orderBy("createdAt", Query.Direction.DESCENDING)
        val snapshot =
            when (lastDocumentSnapshot) {
              null -> query.limit(50).get().await()
              else -> query.startAfter(lastDocumentSnapshot).limit(50).get().await()
            }
        return@firestoreCallWithCatchError snapshot.documents.map { deserializeNews(it) }
      } ?: emptyList()

  override suspend fun filterNewsBasedOnAssociations(
      lastDocumentSnapshot: DocumentSnapshot?,
      userId: String
  ): List<News> =
      firestoreCallWithCatchError {
        val associations =
            DataCache.currentUser.value.following +
                DataCache.currentUser.value.associations.map { it.first }
        val news =
            getAllNews(lastDocumentSnapshot).filter { news ->
              associations.isEmpty() || news.associationId in associations
            }
        return@firestoreCallWithCatchError news
      } ?: emptyList()

  @OptIn(DelicateCoroutinesApi::class)
  override fun createNews(news: News, onSucess: () -> Unit, onError: (String) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
      firestoreCallWithCatchError {
        firestore
            .collection("news")
            .document(news.id)
            .set(serialize(news))
            .addOnSuccessListener { onSucess() }
            .addOnFailureListener { onError(it.message ?: "Error") }
      }
    }
  }

  override suspend fun deleteNews(newsId: String) {
    firestoreCallWithCatchError { firestore.collection("news").document(newsId).delete().await() }
  }

  override suspend fun getNews(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<News> =
      firestoreCallWithCatchError {
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
        return@firestoreCallWithCatchError snapshot.documents.map { deserializeNews(it) }
      } ?: emptyList()

  override suspend fun getEventsFromAnAssociation(
      associationId: String,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> =
      firestoreCallWithCatchError {
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
        return@firestoreCallWithCatchError snapshot.documents.map { deserializeEvent(it) }
      } ?: emptyList()

  override suspend fun getEventsFromAssociations(
      associationIds: List<String>,
      lastDocumentSnapshot: DocumentSnapshot?
  ): List<Event> =
      firestoreCallWithCatchError {
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
        return@firestoreCallWithCatchError snapshot.documents.map { deserializeEvent(it) }
      } ?: emptyList()

  override suspend fun createEvent(event: Event, onSuccess: () -> Unit, onError: (String) -> Unit) {
    firestoreCallWithCatchError {
      firestore
          .collection("events")
          .document(event.id)
          .set(serialize(event))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError("Error") }
    }
  }

  override suspend fun followAssociation(
      associationId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestoreCallWithCatchError {
        val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
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
    firestoreCallWithCatchError {
        val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
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
    firestoreCallWithCatchError {
        val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
            .update("savedEvents", FieldValue.arrayUnion(eventId))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError("Saving Error") }
      }
  }

  override suspend fun saveNews(newsId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    firestoreCallWithCatchError {
        val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
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
    firestoreCallWithCatchError {
      val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
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
    firestoreCallWithCatchError {
        val uid = auth.currentUser?.uid ?: ""
        firestore
            .collection("users")
            .document(uid)
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
    firestoreCallWithCatchError {
      firestore
          .collection("users")
          .document(userId)
          .update(
              "associations",
              FieldValue.arrayUnion(
                  mapOf(
                      "assoId" to triple.first,
                      "position" to triple.second,
                      "rank" to triple.third)))
          .addOnSuccessListener { onSuccess() }
          .addOnFailureListener { onError(it.message ?: "") }
      firestore
          .collection("associations/${triple.first}/committee")
          .add(mapOf("memberId" to userId, "position" to triple.second, "rank" to triple.third))
    }
  }

  override suspend fun quitAssociation(
      assoId: String,
      userId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    firestoreCallWithCatchError {
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
  }

  override suspend fun updateBanner(associationId: String, banner: Uri) {
    firestoreCallWithCatchError {
      firestore
          .collection("associations")
          .document(associationId)
          .update("banner", banner.toString())
          .await()
    }
    val query = firestore.collection("tickets").whereEqualTo("userId", userId)
    val snapshot =
        when (lastDocumentSnapshot) {
          null -> query.limit(10).get().await()
          else -> query.startAfter(lastDocumentSnapshot).limit(10).get().await()
        }
    return snapshot.documents.map { deserializeTicket(it) }
  }

  override suspend fun getTicketsFromUserIdAndEventId(
      userId: String,
      eventId: String
  ): List<Ticket> {
    val query =
        firestore
            .collection("tickets")
            .whereEqualTo("userId", userId)
            .whereEqualTo("eventId", eventId)
    val snapshot = query.get().await()
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

  override suspend fun getTicketsFromEventId(eventId: String): List<Ticket> =
      firestoreCallWithCatchError {
        val query = firestore.collection("tickets").whereEqualTo("eventId", eventId)
        val snapshot = query.get().await()
        return@firestoreCallWithCatchError snapshot.documents.map { deserializeTicket(it) }
      } ?: emptyList()
}
