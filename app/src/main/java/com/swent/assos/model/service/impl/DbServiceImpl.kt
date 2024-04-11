package com.swent.assos.model.service.impl

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.Association
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.HiltAndroidApp
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
          acronym = it.getString("acronym") ?: "",
          fullname = it.getString("fullname") ?: "",
          url = it.getString("url") ?: "",
          documentSnapshot = it)
    }
  }
}
