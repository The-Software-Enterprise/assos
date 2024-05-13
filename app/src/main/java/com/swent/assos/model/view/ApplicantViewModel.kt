package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ApplicantViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _applicants = MutableStateFlow(emptyList<Applicant>())
  val applicants = _applicants.asStateFlow()

  fun getApplicants(eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      _applicants.value = dbService.getApplicantsByEventId(eventId)
    }
  }

  fun acceptStaff(applicantId: String, eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.acceptStaff(applicantId = applicantId, eventId = eventId)
    }
  }

  fun unAcceptStaff(applicantId: String, eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.unAcceptStaff(applicantId = applicantId, eventId = eventId)
    }
  }
}
