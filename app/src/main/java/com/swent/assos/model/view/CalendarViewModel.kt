package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CalendarViewModel
@Inject
constructor(val dbService: DbService, @IoDispatcher private val ioDispatcher: CoroutineDispatcher) :
    ViewModel() {

  private val _events = MutableStateFlow(emptyList<Event>())
  val events = _events.asStateFlow()

  private val _tomorrowEvents = MutableStateFlow(emptyList<Pair<String, Event>>())
  val tomorrowEvents = _tomorrowEvents.asStateFlow()

  private val _selectedDate = MutableStateFlow(LocalDate.now())
  val selectedDate = _selectedDate.asStateFlow()

  private val _selectedEvents = MutableStateFlow(emptyList<Event>())
  val selectedEvents = _selectedEvents.asStateFlow()

  private var _loading = false

  private val user = DataCache.currentUser

  fun updateEvents() {
    _loading = true
    viewModelScope.launch(ioDispatcher) {
      dbService
          .getEventsFromAssociations(
              user.value.following + user.value.associations.map { it.first }, null)
          .let {
            _events.value = it
            _tomorrowEvents.value =
                it.map { event ->
                      Pair(dbService.getAssociationById(event.associationId).acronym, event)
                    }
                    .filter { event ->
                      event.second.startTime?.toLocalDate() == LocalDate.now().plusDays(1)
                    }
          }
    }
    _loading = false
  }

  fun loadMoreEvents() {
    if (!_loading) {
      _loading = true
      viewModelScope.launch(ioDispatcher) {
        val lastDocumentSnapshot = _events.value.lastOrNull()?.documentSnapshot
        dbService
            .getEventsFromAssociations(
                user.value.following + user.value.associations.map { it.first },
                lastDocumentSnapshot)
            .let {
              _events.value += it
              _events.value = _events.value.distinct()
              _tomorrowEvents.value +=
                  it.map { event ->
                        Pair(dbService.getAssociationById(event.associationId).acronym, event)
                      }
                      .filter { event ->
                        event.second.startTime?.toLocalDate() == LocalDate.now().plusDays(1)
                      }
              if (it.isNotEmpty()) {
                _loading = false
              }
            }
      }
    }
  }

  fun updateSelectedDate(date: LocalDate) {
    _selectedDate.value = date
  }

  fun filterEvents() {
    _selectedEvents.value =
        _events.value.filter {
          if (it.startTime == null || it.endTime == null) {
            false
          } else {
            it.startTime!!.isAfter(selectedDate.value.atStartOfDay()) &&
                it.endTime!!.isBefore(selectedDate.value.atStartOfDay().plusDays(1))
          }
        }
  }
}
