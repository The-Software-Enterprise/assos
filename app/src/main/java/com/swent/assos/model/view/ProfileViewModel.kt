package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
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
  private val _followedAssociationIDs =
      listOf(
          "1UYICvqVKbImYMNK3Sz3",
          "5Ala8A5MEmoFe5JGJxJV",
          "dPJWcdE67iXbcmzdBARD",
          "uOJXi0JzcNgrgScwi3ah",
          "idThPItJ245oI6SbyGbj",
          "gaFf6o6Sp2f3ztFDdIrY",
          "MCRM0iDOfGZKKiOXsB23",
          "PZfOVbkjdNJ9ppG6PqOa",
          "GmC5uA4qyRmswqiUl7sp",
          "1GysfTi14xSiW4Te9fUH")
  private val _memberAssociationIDs = listOf("1UYICvqVKbImYMNK3Sz3", "5Ala8A5MEmoFe5JGJxJV")

  private val _followedAssociations = MutableStateFlow(emptyList<Association>())
  val followedAssociations = _followedAssociations.asStateFlow()

  private val _memberAssociations = MutableStateFlow(emptyList<Association>())
  val memberAssociations = _memberAssociations.asStateFlow()

  private var _loading = false

  init {
    viewModelScope.launch(ioDispatcher) {
      _followedAssociationIDs.forEach { id ->
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
