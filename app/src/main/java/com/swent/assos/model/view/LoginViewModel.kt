package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.data.Association
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

    var user = User(0, "", "", "", emptyList(), emptyList())

    fun updateUserInfo (){
        val docRef = firestoreInstance.collection("users").document(hashEmail(user.email).toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    user = User(
                        document.data?.get("uid") as Int,
                        document.data?.get("firstname") as String,
                        document.data?.get("lastname") as String,
                        document.data?.get("email") as String,
                        document.data?.get("associations") as List<Triple<String,String,Int>>,
                        document.data?.get("following") as List<String>
                    )
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    fun hashEmail(email: String): Int {
        val bytes = email.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        // get the integer value
        return digest.fold(0) { acc, byte -> acc * 256 + byte.toInt() }
    }

    //fun goToSignUp() = navController.navigate("SignUp")
    //fun goToSignIn() = navController.navigate("Login")

    fun signIn(email: String, password: String) = accountService.signIn(email, password)

    fun signUp(email: String, password: String) = accountService.signUp(email, password)
}
