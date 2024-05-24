package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ApplicationViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _joiningApplications = MutableStateFlow(emptyList<Pair<String, Applicant>>())
  val joiningApplications = _joiningApplications.asStateFlow()

  private val _staffingApplications = MutableStateFlow(emptyList<Pair<String, Applicant>>())
  val staffingApplications = _staffingApplications.asStateFlow()

  fun updateJoiningApplications() {
    viewModelScope.launch(ioDispatcher) {
      for (application in DataCache.currentUser.value.appliedAssociation) {
        val joiningApplication =
            Pair(
                dbService.getAssociationById(application).acronym,
                dbService.getApplicantByAssoIdAndUserId(
                    application, DataCache.currentUser.value.id))
        if (_joiningApplications.value.contains(joiningApplication)) {
          continue
        }
        _joiningApplications.value += joiningApplication
      }
    }
  }

  fun updateStaffingApplications() {
    viewModelScope.launch(ioDispatcher) {
      for (application in DataCache.currentUser.value.appliedStaffing) {
        val staffingApplication =
            Pair(
                dbService.getEventById(application).title,
                dbService.getApplicantByEventIdAndUserId(
                    application, DataCache.currentUser.value.id))
        if (_staffingApplications.value.contains(staffingApplication)) {
          continue
        }
        _staffingApplications.value += staffingApplication
      }
    }
  }
}
