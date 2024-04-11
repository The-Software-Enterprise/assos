package com.swent.assos.model.service.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.swent.assos.config.Config
import com.swent.assos.model.service.AuthService
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AuthService {

  // TODO: if you are using the firestore emulator, make sure to change the host and port to match
  // your firestore emulator
  val config = Config()

  // if config.get_demo() is true, we are in debug mode and we want to use the firestore emulator
  val ok =
      if (config.get_demo()) {
        auth.useEmulator("10.0.2.2", 9099)
      } else {}
  // val ok = auth.useEmulator("10.0.2.2",9099)

  override val currentUser: Flow<FirebaseUser>
    get() = callbackFlow {
      val listener =
          FirebaseAuth.AuthStateListener { auth -> auth.currentUser?.let { this.trySend(it) } }
      auth.addAuthStateListener(listener)
      awaitClose { auth.removeAuthStateListener(listener) }
    }

  override fun signIn(email: String, password: String): Task<AuthResult> {
    return auth.signInWithEmailAndPassword(email, password)
  }

  override fun signUp(email: String, password: String): Task<AuthResult> {
    return auth.createUserWithEmailAndPassword(email, password)
  }

  override fun signOut() {
    auth.signOut()
  }
}
