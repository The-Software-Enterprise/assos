package com.swent.assos

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
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

    val mockAuth = mockk<FirebaseAuth>()
    val mockUser = mockk<FirebaseUser>()
    coEvery { mockQuerySnapshot.documents[0] } returns mockDocumentSnapshot
    coEvery { mockDocumentSnapshot.id } returns ""
    coEvery { mockAuth.currentUser } returns mockUser
    coEvery { mockUser.uid } returns "id"

    val dbService = DbServiceImpl(mockFirestore, mockAuth)

    dbService.getUser("id")

    dbService.getAllAssociations(null)
    dbService.getAllAssociations(mockDocumentSnapshot)

    dbService.getAssociationById("id")

    dbService.getAllNews(null)
    dbService.getAllNews(mockDocumentSnapshot)

    dbService.filterNewsBasedOnAssociations(null, "id")
    dbService.filterNewsBasedOnAssociations(mockDocumentSnapshot, "id")

    dbService.getNews("id", null)
    dbService.getNews("id", mockDocumentSnapshot)

    dbService.createNews(News(), {}, {})

    dbService.getEventsFromAnAssociation("id", null)
    dbService.getEventsFromAnAssociation("id", mockDocumentSnapshot)

    dbService.getEventsFromAssociations(listOf("id"), null)
    dbService.getEventsFromAssociations(listOf("id"), mockDocumentSnapshot)

    dbService.createEvent(Event(id = "id"), {}, {})

    dbService.followAssociation("id", {}, {})

    dbService.unfollowAssociation("id", {}, {})

    dbService.getUserByEmail("someone.weno@epfl.ch", {}, {})

    return@runBlocking
  }
}
