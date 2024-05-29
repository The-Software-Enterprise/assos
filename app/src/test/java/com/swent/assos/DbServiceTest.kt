package com.swent.assos

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.service.impl.DbServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DbServiceTest {

  @Test
  fun testAllMethodsCrash() = runBlocking {
    val mockFirestore = mockk<FirebaseFirestore>()
    val mockDocumentSnapshot = mockk<DocumentSnapshot>(relaxed = true)
    val mockQuerySnapshot = mockk<QuerySnapshot>(relaxed = true)

    coEvery { mockFirestore.collection(any()).get() } returns Tasks.forResult(mockQuerySnapshot)
    coEvery { mockFirestore.collection(any()).orderBy(any<String>()).get() } returns
        Tasks.forResult(mockQuerySnapshot)
    coEvery { mockFirestore.collection(any()).limit(any()).get() } returns
        Tasks.forResult(mockQuerySnapshot)
    coEvery { mockFirestore.collection(any()).orderBy(any<String>()).limit(any()).get() } returns
        Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore.collection(any()).orderBy(any<String>()).startAfter(any()).limit(any()).get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery { mockFirestore.collection(any()).add(any()) } returns Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).get() } returns
        Tasks.forResult(mockDocumentSnapshot)
    coEvery { mockFirestore.collection(any()).document(any()).update(any<String>(), any()) } returns
        Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).set(any()) } returns
        Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).delete() } returns
        Tasks.forResult(null)
    coEvery { mockFirestore.document(any()).update(any<String>(), any()) } returns
        Tasks.forResult(null)
    coEvery {
      mockFirestore
          .collection(any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .orderBy(any<String>(), Query.Direction.DESCENDING)
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .orderBy(any<String>(), Query.Direction.DESCENDING)
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereIn(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereIn(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereIn(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.DESCENDING)
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.DESCENDING)
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.DESCENDING)
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .whereGreaterThan(any<String>(), any())
          .orderBy(any<String>(), Query.Direction.ASCENDING)
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery { mockFirestore.collection(any()).whereEqualTo(any<String>(), any()).get() } returns
        Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .whereEqualTo(any<String>(), any())
          .whereEqualTo(any<String>(), any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore.collection(any()).whereEqualTo(any<String>(), any()).limit(any()).get()
    } returns Tasks.forResult(mockQuerySnapshot)
    coEvery {
      mockFirestore
          .collection(any())
          .whereEqualTo(any<String>(), any())
          .startAfter(any())
          .limit(any())
          .get()
    } returns Tasks.forResult(mockQuerySnapshot)

    coEvery { mockFirestore.document(any<String>()).get() } returns
        Tasks.forResult(mockDocumentSnapshot)

    coEvery { mockFirestore.collection(any()).whereEqualTo(any<String>(), any()).get() } returns
        Tasks.forResult(mockQuerySnapshot)

    val mockAuth = mockk<FirebaseAuth>()
    val mockUser = mockk<FirebaseUser>()
    coEvery { mockQuerySnapshot.documents[0] } returns mockDocumentSnapshot
    coEvery { mockDocumentSnapshot.id } returns "id"
    coEvery { mockAuth.currentUser } returns mockUser
    coEvery { mockUser.uid } returns "id"

    val mockContext = mockk<Context>()
    coEvery { mockContext.applicationContext } returns mockContext
    coEvery { mockContext.getSystemService(any()) } returns null

    val dbService = DbServiceImpl(mockFirestore, mockAuth, mockContext)

    dbService.getUser("id")
    dbService.getUserByEmail("email", { null }, { null })
    dbService.getAssociationById("id")
    dbService.getEventById("id")
    dbService.applyStaffing("id", "id", { null }, { null })
    dbService.applyJoinAsso("id", "id", { null }, { null })
    dbService.getAllNews(null)
    dbService.getAllNews(mockDocumentSnapshot)
    dbService.filterNewsBasedOnAssociations(null, "id")
    dbService.filterNewsBasedOnAssociations(mockDocumentSnapshot, "id")
    dbService.addApplicant("toWhat", "id", "id", { null }, { null })
    dbService.unAcceptStaff("applicantId", "eventId")
    dbService.acceptStaff("applicantId", "eventId")
    dbService.createNews(News(), { null }, { null })
    dbService.getNews("id", null)
    dbService.getNews("id", mockDocumentSnapshot)
    dbService.getEventsFromAnAssociation("id", null)
    dbService.getEventsFromAnAssociation("id", mockDocumentSnapshot)
    dbService.getEventsFromAssociations(listOf("id"), null)
    dbService.getEventsFromAssociations(listOf("id"), mockDocumentSnapshot)
    dbService.createEvent(Event(id = "id"), { null }, { null })
    dbService.getEventFromId("id")
    dbService.followAssociation("id", { null }, { null })
    dbService.unfollowAssociation("id", { null }, { null })
    dbService.addTicketToUser("id", "id", ParticipationStatus.Staff)
    dbService.removeTicketFromUser("id", "id", ParticipationStatus.Staff)
    dbService.joinAssociation(Triple("id", "id", 0), "id", { null }, { null })
    dbService.updateBanner("id", Uri.EMPTY)
    dbService.getTickets("id", null)
    dbService.getTickets("id", mockDocumentSnapshot)
    dbService.getTicketFromId("id")
    dbService.getApplicantsByEventId("id")
    dbService.getApplicantsByAssoId("id")
    dbService.acceptApplicant("id", "id")
    dbService.unAcceptApplicant("id", "id")
    dbService.quitAssociation("id", "id", { null }, { null })

    dbService.getUserByEmail("someone.weno@epfl.ch", {}, {})

    return@runBlocking
  }
}
