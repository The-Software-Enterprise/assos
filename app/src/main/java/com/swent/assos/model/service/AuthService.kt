package com.swent.assos.model.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthService {
  val currentUser: Flow<FirebaseUser>

  fun signIn(email: String, password: String): Task<AuthResult>
  fun signUp(email: String, password: String): Task<AuthResult>

  fun signOut()
}
