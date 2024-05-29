package com.swent.assos.model.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
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

  private val _savedEvents = MutableStateFlow(emptyList<Event>())
  val savedEvents = _savedEvents.asStateFlow()

  private val _savedNews = MutableStateFlow(emptyList<News>())
  val savedNews = _savedNews.asStateFlow()

  private var _selectedOption by mutableStateOf("Events")
  // val selectedOption = _selectedOption.asStateFlow()

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

        currentUser.savedNews.forEach { newsId ->
          dbService.getNews(newsId).let { it ->
            _savedNews.value += it
            _savedNews.value = _savedNews.value.distinct().sortedBy { it.createdAt }
          }
        }

        currentUser.savedEvents.forEach { eventId ->
          dbService.getEventById(eventId).let { it ->
            _savedEvents.value += it
            _savedEvents.value = _savedEvents.value.distinct().sortedBy { it.startTime }
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
            //
            _memberAssociations.value = _memberAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _savedEvents.value = emptyList()
        currentUser.savedEvents.forEach { id ->
          dbService.getEventById(id).let {
            _savedEvents.value += it
            _savedEvents.value = _savedEvents.value.distinct().sortedBy { it.startTime }
          }
        }
        _savedNews.value = emptyList()
        currentUser.savedNews.forEach { id ->
          dbService.getNews(id).let {
            _savedNews.value += it
            _savedNews.value = _savedNews.value.distinct().sortedBy { it.createdAt }
          }
        }
        _loading.value = false
      }
    }
    viewModelScope.launch(ioDispatcher) { _update.value = false }
  }
}
