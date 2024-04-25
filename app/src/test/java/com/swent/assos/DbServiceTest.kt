package com.swent.assos

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.swent.assos.model.data.News
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.impl.DbServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    coEvery { mockFirestore.collection(any()).add(any()) } returns Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).get() } returns
        Tasks.forResult(mockDocumentSnapshot)
    coEvery { mockFirestore.collection(any()).document(any()).update(any<String>(), any()) } returns
        Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).set(any()) } returns
        Tasks.forResult(null)
    coEvery { mockFirestore.collection(any()).document(any()).delete() } returns
        Tasks.forResult(null)

    val mockAuth = mockk<FirebaseAuth>()
    val mockUser = mockk<FirebaseUser>()

    coEvery { mockAuth.currentUser } returns mockUser
    coEvery { mockUser.uid } returns "id"

    val dbService = DbServiceImpl(mockFirestore, mockAuth)

    dbService.getUser("id")
    dbService.createNews(News(), {}, {})
    dbService.updateNews(News(), {}, {})
    dbService.deleteNews(News(), {}, {})
    dbService.getAllEvents()
    dbService.getAllAssociations(null)
    dbService.getAssociationById("id")
    dbService.followAssociation("id", {}, {})
    dbService.unfollowAssociation("id", {}, {})
  }
}
