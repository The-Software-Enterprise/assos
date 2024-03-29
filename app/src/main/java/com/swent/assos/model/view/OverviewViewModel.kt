package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OverviewViewModel @Inject constructor(
  private val dbService: DbService,
  private val authService: AuthService
): ViewModel() {
  private val _allAssociations = MutableStateFlow(emptyList<Association>())
  val allAssociations = _allAssociations.asStateFlow()

  private val _filteredAssociations = MutableStateFlow(emptyList<Association>())
  val filteredAssociations = _filteredAssociations.asStateFlow()

  init {
      viewModelScope.launch(Dispatchers.IO) {
        dbService.getAllAssociations().let {
          _allAssociations.value = it
          _filteredAssociations.value = it
        }
      }
  }

  fun filterOnSearch(query: String) {
    _filteredAssociations.value = _allAssociations.value.filter {
      it.acronym.contains(query, ignoreCase = true) || it.fullname.contains(query, ignoreCase = true)
    }
  }



}




