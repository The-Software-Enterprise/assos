package com.swent.assos.model.service

import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.Association

interface DbService {
  suspend fun getAllAssociations(lastDocumentSnapshot: DocumentSnapshot?): List<Association>
}
