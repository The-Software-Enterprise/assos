package com.swent.assos

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.News
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.impl.DbServiceImpl
import io.mockk.mockk
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DbServiceTest {

    @Test
    fun testAllMethodsCrash() = runBlocking {
        val mockFirestore = mockk<FirebaseFirestore>()
        val mockDocumentSnapshot = mockk<DocumentSnapshot>(relaxed = true)

        coEvery { mockFirestore.collection(any()).document(any()).get() } returns mockk {
            every { isSuccessful } returns true
            every { result } returns mockk {
                every { data } returns mapOf("key" to "value")
            }
        }
        every { mockFirestore.collection(any()).document(any()).get() } returns Tasks.forResult(mockDocumentSnapshot)

        val mockAuth = mockk<AuthService>()

        val dbService = DbServiceImpl(mockFirestore, mockAuth)

        dbService.getUser("id")
        dbService.createNews(News(), {}, {})
        dbService.updateNews(News(), {}, {})
        dbService.deleteNews(News(), {}, {})
        dbService.getAllAssociations(null)
        dbService.getAssociationById("id")
        dbService.getAllEvents()
        dbService.getAllEvents()
        dbService.followAssociation("id", {}, {})
        dbService.unfollowAssociation("id", {}, {})
    }
}