package com.swent.assos.model.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.EventFieldType
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EventViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val storageService: StorageService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _event = MutableStateFlow(Event(id = generateUniqueID(), associationId = ""))
  val event = _event.asStateFlow()

  private val _fieldType = MutableStateFlow(EventFieldType.TEXT)
  val fieldType = _fieldType.asStateFlow()

  private val _hourFormat = MutableStateFlow(HourFormat.AM)
  val hourFormat = _hourFormat.asStateFlow()

  init {
    viewModelScope.launch(ioDispatcher) {}
  }

  fun switchHourFormat() {
    _hourFormat.value =
        when (_hourFormat.value) {
          HourFormat.AM -> HourFormat.PM
          HourFormat.PM -> HourFormat.AM
        }
  }

  fun resetHourFormat() {
    _hourFormat.value = HourFormat.AM
  }

  fun switchFieldType() {
    _fieldType.value =
        EventFieldType.entries[(_fieldType.value.ordinal + 1) % EventFieldType.entries.size]
  }

  fun resetFieldType() {
    _fieldType.value = EventFieldType.TEXT
  }

  fun createEvent(onSuccess: () -> Unit) {
    val event = _event.value
    viewModelScope.launch(ioDispatcher) {
      storageService.uploadFile(
          event.image,
          "events/${event.id}",
          onSuccess = {
            event.image = it
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
}

enum class HourFormat {
  AM,
  PM
}
