package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

@HiltViewModel
class NFCViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  fun checkTicket(ticketId: String, validIDs: List<String>): Boolean {
    return validIDs.contains(ticketId)
  }

  fun collectTickets(eventID: String, onSuccess: (List<String>) -> Unit) {
    viewModelScope.launch(ioDispatcher) {
      onSuccess(dbService.getTicketsFromEventId(eventID).map { it.id })
    }
  }
}
