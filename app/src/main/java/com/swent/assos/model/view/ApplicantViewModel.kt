package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.ParticipationStatus
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

  private val _update = MutableStateFlow(false)
  val update = _update.asStateFlow()

  private val _updateStaffing = MutableStateFlow(false)
  val updateStaffing = _updateStaffing.asStateFlow()

  fun getApplicantsForStaffing(eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      _applicants.value = dbService.getApplicantsByEventId(eventId)
    }
  }

  fun getApplicantsForJoining(assoId: String) {
    viewModelScope.launch(ioDispatcher) {
      _applicants.value = dbService.getApplicantsByAssoId(assoId)
    }
  }

  fun acceptStaff(applicantId: String, eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.acceptStaff(applicantId = applicantId, eventId = eventId)
      dbService.addTicketToUser(
          applicantId = applicantId, eventId = eventId, status = ParticipationStatus.Staff)
    }
  }

  fun unAcceptStaff(applicantId: String, eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.unAcceptStaff(applicantId = applicantId, eventId = eventId)
      dbService.removeTicketFromUser(
          applicantId = applicantId, eventId = eventId, status = ParticipationStatus.Staff)
    }
  }

  fun acceptApplicant(assoId: String, applicantId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.acceptApplicant(applicantId = applicantId, assoId = assoId)
    }
  }

  fun unAcceptApplicant(applicantId: String, assoId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.unAcceptApplicant(applicantId = applicantId, assoId = assoId)
    }
  }

  fun rejectApplicant(applicantId: String, assoId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.rejectApplicant(applicantId = applicantId, assoId = assoId)
    }
  }

  fun rejectStaff(applicantId: String, eventId: String) {
    viewModelScope.launch { dbService.rejectStaff(applicantId = applicantId, eventId = eventId) }
  }

  fun deleteRequest(userId: String, assoId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.removeJoinApplication(
          assoId = assoId, userId = userId, onSuccess = {}, onError = {})
    }
    viewModelScope.launch(ioDispatcher) { _update.value = true }
  }

  fun deleteStaffRequest(userId: String, eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.removeStaffingApplication(
          eventId = eventId, userId = userId, onSuccess = {}, onError = {})
    }
    viewModelScope.launch(ioDispatcher) { _updateStaffing.value = true }
  }

  fun updateApplicants(assoId: String) {
    viewModelScope.launch(ioDispatcher) {
      _applicants.value = dbService.getApplicantsByAssoId(assoId)
      _update.value = false
    }
  }

  fun updateStaffing(eventId: String) {
    viewModelScope.launch(ioDispatcher) {
      _applicants.value = dbService.getApplicantsByEventId(eventId)
      _updateStaffing.value = false
    }
  }
}
