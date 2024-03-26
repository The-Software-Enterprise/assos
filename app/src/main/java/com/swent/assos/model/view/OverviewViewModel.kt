package com.swent.assos.model.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel
@Inject
constructor(
  private val dbService: DbService,
  private val authService: AuthService
) : ViewModel() {
  val _allAssociations = MutableStateFlow(emptyList<Association>())
  val allAssociations = _allAssociations.asStateFlow()

  init {
    viewModelScope.launch(Dispatchers.IO) {
      dbService.getAllAssociations().let {
        _allAssociations.value = it
      }
    }
  }
}