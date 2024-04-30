package com.swent.assos.model.data

import kotlinx.coroutines.flow.MutableStateFlow

object DataCache {
  val currentUser: MutableStateFlow<User> = MutableStateFlow(User())

  fun signOut() {
    currentUser.value = User()
  }
}
