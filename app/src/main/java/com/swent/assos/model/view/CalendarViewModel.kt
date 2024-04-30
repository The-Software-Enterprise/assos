package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class CalendarViewModel
@Inject
constructor(
  val dbService: DbService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher) : ViewModel() {

  private val _events = MutableStateFlow(emptyList<Event>())
  val events = _events.asStateFlow()
  private val _tomorrowEvents = MutableStateFlow(emptyList<Pair<String, Event>>())
  val tomorrowEvents = _tomorrowEvents.asStateFlow()

  private val user = DataCache.currentUser

  init {
    viewModelScope.launch(ioDispatcher) {
      for (association in user.value.following) {
        dbService.getEvents(association, null).let {
          _events.value += it
          _tomorrowEvents.value += it.map {
            event -> Pair(dbService.getAssociationById(event.associationId).fullname, event)
          }.filter { event -> event.second.startTime?.toLocalDate() == LocalDate.now().plusDays(1) }
        }
      }
      for (association in user.value.associations) {
        dbService.getEvents(association.first, null).let {
          _events.value += it
          _tomorrowEvents.value += it.map {
              event -> Pair(dbService.getAssociationById(event.associationId).fullname, event)
          }.filter { event -> event.second.startTime?.toLocalDate() == LocalDate.now().plusDays(1) }
        }
      }
    }
  }
}
