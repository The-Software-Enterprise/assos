package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.data.User
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val storageService: DbService, private val accountService: AuthService) :
    ViewModel() {
  val currentUser = accountService.currentUser

  val firestoreInstance = Firebase.firestore

  var user = User("", "", "", "", emptyList(), mutableListOf())

  fun hashEmail(email: String): Int {
    val bytes = email.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    // get the integer value
    return digest.fold(0) { acc, byte -> acc * 256 + byte.toInt() }
  }

  // fun goToSignUp() = navController.navigate("SignUp")
  // fun goToSignIn() = navController.navigate("Login")

  fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
    accountService.signIn(email, password).addOnCompleteListener {
      if (it.isSuccessful) {
        accountService
      }
      callback(it.isSuccessful)
    }
  }

  fun signUp(email: String, password: String, callback: (Boolean) -> Unit) {
    accountService.signUp(email, password).addOnCompleteListener { callback(it.isSuccessful) }
  }

  fun signOut() {
    accountService.signOut()
  }
}
