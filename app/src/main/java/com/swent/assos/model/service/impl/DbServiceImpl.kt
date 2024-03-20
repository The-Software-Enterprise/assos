package com.swent.assos.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import javax.inject.Inject

class DbServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AuthService,
) : DbService {
}
