package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val authService: AuthService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  /* TODO: Link to dataBase the membership,
  for now all users are "Balélec" & "The Consulting Society" members */
  private val _memberAssociationIDs = listOf("1UYICvqVKbImYMNK3Sz3", "5Ala8A5MEmoFe5JGJxJV")

  private val _followedAssociations = MutableStateFlow(emptyList<Association>())
  val followedAssociations = _followedAssociations.asStateFlow()

  private val _memberAssociations = MutableStateFlow(emptyList<Association>())
  val memberAssociations = _memberAssociations.asStateFlow()

  private var _loading = false

  fun signOut() {
    try {
      authService.signOut()
    } catch (e: Exception) {
      println("Error while signing out")
    }
  }

  init {
    viewModelScope.launch(ioDispatcher) {
      DataCache.currentUser.collect { currentUser ->
        currentUser.following.forEach { id ->
          dbService.getAssociationById(id).let {
            _followedAssociations.value += it
            _followedAssociations.value =
                _followedAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _memberAssociationIDs.forEach { id ->
          dbService.getAssociationById(id).let {
            _memberAssociations.value += it
            _memberAssociations.value = _memberAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
      }
    }
  }
}
