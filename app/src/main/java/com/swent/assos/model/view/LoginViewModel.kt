package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val storageService: DbService,
    private val accountService: AuthService
) : ViewModel() {
    val currentUser = accountService.currentUser
    fun signIn(email: String, password: String) = accountService.signIn(email, password)
    fun signUp(email: String, password: String) = accountService.signUp(email, password)
}
