package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.MIN_LOADED_ITEMS
import com.swent.assos.model.data.Association
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExplorerViewModel
@Inject
constructor(private val dbService: DbService, private val authService: AuthService) : ViewModel() {
  private val _allAssociations = MutableStateFlow(emptyList<Association>())
  val allAssociations = _allAssociations.asStateFlow()

  private val _filteredAssociations = MutableStateFlow(emptyList<Association>())
  val filteredAssociations = _filteredAssociations.asStateFlow()

  private val _researchQuery = MutableStateFlow("")
  val researchQuery = _researchQuery.asStateFlow()

  private var _loading = false

  private var _loadingDisplay = MutableStateFlow(false)
  val loading = _loadingDisplay.asStateFlow()

  init {
    _loadingDisplay.value = true
    viewModelScope.launch(Dispatchers.IO) {
      dbService.getAllAssociations(null).let {
        _allAssociations.value = it
        filterOnSearch(_researchQuery.value)
        _loadingDisplay.value = false
      }
    }
  }

  fun filterOnSearch(query: String) {
    _researchQuery.value = query
    _filteredAssociations.value =
        _allAssociations.value.filter {
          it.acronym.contains(query, ignoreCase = true) ||
              it.fullname.contains(query, ignoreCase = true)
        }
    if (_filteredAssociations.value.size < MIN_LOADED_ITEMS) {
      loadMoreAssociations()
    }
  }

  fun loadMoreAssociations() {
    if (!_loading) {
      _loading = true
      viewModelScope.launch(Dispatchers.IO) {
        val lastDocumentSnapshot = _allAssociations.value.lastOrNull()?.documentSnapshot
        dbService.getAllAssociations(lastDocumentSnapshot).let {
          _allAssociations.value += it
          _allAssociations.value = _allAssociations.value.distinct()
          filterOnSearch(_researchQuery.value)
          if (it.isNotEmpty()) {
            _loading = false
          }
          if (_researchQuery.value.isNotEmpty() &&
              (_filteredAssociations.value.isEmpty() || _filteredAssociations.value.size < 10)) {
            loadMoreAssociations()
          }
        }
      }
    }
  }
}
