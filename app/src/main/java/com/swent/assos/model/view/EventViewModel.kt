package com.swent.assos.model.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val storageService: StorageService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _event = MutableStateFlow(Event(id = generateUniqueID()))
  val event = _event.asStateFlow()

  private var _loadingDisplay = MutableStateFlow(true)
  val loading = _loadingDisplay.asStateFlow()

  private var _appliedStaff = MutableStateFlow(false)
  val appliedStaff = _appliedStaff.asStateFlow()

  fun clear() {
    _event.value = Event(id = "CLEARED")
  }

  fun getEvent(eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      _event.value = dbService.getEventById(eventId)
      _appliedStaff.update { DataCache.currentUser.value.appliedStaffing.contains(_event.value.id) }
    }
  }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch(ioDispatcher) {
        dbService.deleteEvent(eventId)
        }
    }

  fun createEvent(onSuccess: () -> Unit, onError: () -> Unit) {
    val event = _event.value
    viewModelScope.launch(ioDispatcher) {
      storageService.uploadFiles(
          listOf(event.image) +
              event.fields.filterIsInstance<Event.Field.Image>().flatMap { it.uris },
          "events/${event.id}",
          onSuccess = { uris ->
            if (uris.isNotEmpty()) {
              event.image = uris[0]
              event.fields =
                  event.fields.mapIndexed { index, field ->
                    if (field is Event.Field.Image) {
                      val uriIndex =
                          event.fields
                              .subList(0, index)
                              .filterIsInstance<Event.Field.Image>()
                              .sumOf { it.uris.size }
                      Event.Field.Image(uris.subList(uriIndex + 1, uriIndex + field.uris.size + 1))
                    } else {
                      field
                    }
                  }
            }
            viewModelScope.launch(ioDispatcher) {
              dbService.createEvent(event = event, onSuccess = onSuccess, onError = {})
            }
          },
          onError = {})
    }
  }

  fun setTitle(title: String) {
    _event.value = _event.value.copy(title = title)
  }

  fun setDescription(description: String) {
    _event.value = _event.value.copy(description = description)
  }

  fun setImage(uri: Uri?) {
    if (uri != null) {
      _event.value = _event.value.copy(image = uri)
    }
  }

  fun createTicket(
      email: String,
      onSuccess: () -> Unit,
      onFailure: () -> Unit,
      status: ParticipationStatus
  ) {

    viewModelScope.launch(ioDispatcher) {
      val user = dbService.getUserByEmail(email, onSuccess = onSuccess, onFailure = onFailure)
      if (user.id != "") {
        dbService.addTicketToUser(user.id, _event.value.id, status)
      } else {
        onFailure()
      }
    }
  }

  fun applyStaffing(eventId: String, userId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            appliedStaffing = DataCache.currentUser.value.appliedStaffing + _event.value.id)
    viewModelScope.launch(ioDispatcher) {
      dbService.applyStaffing(
          eventId = _event.value.id,
          userId = userId,
          onSuccess = { _appliedStaff.update { true } },
          onError = {})
    }
  }

  fun removeRequestToStaff(eventId: String, userId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            appliedStaffing = DataCache.currentUser.value.appliedStaffing.filter { it != eventId })
    viewModelScope.launch(ioDispatcher) {
      dbService.removeStaffingApplication(
          eventId = eventId,
          userId = userId,
          onSuccess = { _appliedStaff.update { false } },
          onError = {})
    }
  }

  fun addField(field: Event.Field) {
    val fields = _event.value.fields.toMutableList()
    fields.add(field)
    _event.value = _event.value.copy(fields = fields)
  }

  fun addImagesToField(uris: List<Uri>, index: Int) {
    val fields = _event.value.fields.toMutableList()
    fields[index] = Event.Field.Image((fields[index] as Event.Field.Image).uris + uris)
    _event.value = _event.value.copy(fields = fields)
  }

  fun removeImageFromField(index: Int, uri: Uri) {
    val fields = _event.value.fields.toMutableList()
    fields[index] = Event.Field.Image((fields[index] as Event.Field.Image).uris - uri)
    _event.value = _event.value.copy(fields = fields)
  }

  fun updateFieldTitle(index: Int, title: String) {
    val fields = _event.value.fields.toMutableList()
    (fields[index] as Event.Field.Text).title = title
    _event.value = _event.value.copy(fields = fields, _unused = !_event.value._unused)
  }

  fun updateFieldText(index: Int, text: String) {
    val fields = _event.value.fields.toMutableList()
    (fields[index] as Event.Field.Text).text = text
    _event.value = _event.value.copy(fields = fields, _unused = !_event.value._unused)
  }

  fun setStaffingEnabled(isEnabled: Boolean) {
    _event.value = _event.value.copy(isStaffingEnabled = isEnabled)
  }
}
