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

  private val _followedAssociations = MutableStateFlow(emptyList<Association>())
  val followedAssociations = _followedAssociations.asStateFlow()

  private val _memberAssociations = MutableStateFlow(emptyList<Association>())
  val memberAssociations = _memberAssociations.asStateFlow()

  private val _firstName = MutableStateFlow("")
  val firstName = _firstName.asStateFlow()

  private val _lastName = MutableStateFlow("")
  val lastName = _lastName.asStateFlow()

  private var _loading = MutableStateFlow(true)
  val loading = _loading.asStateFlow()

  private val _update = MutableStateFlow(false)
  val update = _update.asStateFlow()

  fun signOut() {
    try {
      DataCache.signOut()
      authService.signOut()
    } catch (e: Exception) {
      println("Error while signing out")
    }
  }

  init {
    viewModelScope.launch(ioDispatcher) {
      DataCache.currentUser.collect { currentUser ->
        _firstName.value = currentUser.firstName
        _lastName.value = currentUser.lastName

        currentUser.following.forEach { id ->
          dbService.getAssociationById(id).let {
            _followedAssociations.value += it
            _followedAssociations.value =
                _followedAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        currentUser.associations.forEach { (assoId, _, _) ->
          dbService.getAssociationById(assoId).let {
            _memberAssociations.value += it
            _memberAssociations.value = _memberAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _loading.value = false
      }
    }
  }

  fun updateNeeded() {
    viewModelScope.launch(ioDispatcher) { _update.value = true }
  }

  fun updateUser() {
    _loading.value = true
    viewModelScope.launch(ioDispatcher) {
      DataCache.currentUser.collect { currentUser ->
        _firstName.value = currentUser.firstName
        _lastName.value = currentUser.lastName

        _followedAssociations.value = emptyList()
        currentUser.following.forEach { id ->
          dbService.getAssociationById(id).let {
            _followedAssociations.value += it
            _followedAssociations.value =
                _followedAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _memberAssociations.value = emptyList()
        currentUser.associations.forEach { (assoId, _, _) ->
          dbService.getAssociationById(assoId).let {
            _memberAssociations.value += it
            _memberAssociations.value = _memberAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _loading.value = false
      }
    }
    viewModelScope.launch(ioDispatcher) { _update.value = false }
  }
}
