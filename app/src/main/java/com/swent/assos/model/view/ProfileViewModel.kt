package com.swent.assos.model.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
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
class ProfileViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  private val _memberAssociationIDs = listOf("1UYICvqVKbImYMNK3Sz3", "5Ala8A5MEmoFe5JGJxJV")

  private val _followedAssociations = MutableStateFlow(emptyList<Association>())
  val followedAssociations = _followedAssociations.asStateFlow()

  private val _memberAssociations = MutableStateFlow(emptyList<Association>())
  val memberAssociations = _memberAssociations.asStateFlow()

  private var _loading = false

  init {
    viewModelScope.launch(ioDispatcher) {
      DataCache.currentUser.collect { currentUser ->
        Log.d("ProfileViewModel", "currentUser: $currentUser")
        currentUser.following.forEach { id ->
          dbService.getAssociationById(id).let {
            _followedAssociations.value += it
            _followedAssociations.value =
                _followedAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
        _memberAssociationIDs.forEach { id ->
          dbService.getAssociationById(id).let {
            _memberAssociations.value += it
            _memberAssociations.value = _memberAssociations.value.distinct().sortedBy { it.acronym }
          }
        }
      }
    }
  }
}