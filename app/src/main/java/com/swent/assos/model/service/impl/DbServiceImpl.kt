package com.swent.assos.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.Association
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthService,
) : DbService {

    override suspend fun getAllAssociations(): List<Association> {
        return firestore.collection("associations")
            .get()
            .await()
            .documents
            .map { document ->
                Association(
                    id = document.id,
                )}
    }
}
