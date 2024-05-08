package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TicketViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  val currentUser = DataCache.currentUser.asStateFlow()

  private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
  val tickets = _tickets.asStateFlow()

    fun getTickets() {
        viewModelScope.launch(ioDispatcher) {
            _tickets.value = dbService.getTickets(currentUser.value.id, lastDocumentSnapshot = null)
        }
    }


}
