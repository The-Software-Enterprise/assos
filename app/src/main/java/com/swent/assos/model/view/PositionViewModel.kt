package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class PositionViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _position = MutableStateFlow(OpenPositions())
  val position = _position.asStateFlow()

  private val _canDelete = MutableStateFlow(false)
  val canDelete = _canDelete.asStateFlow()

  fun getPosition(associationId: String, positionId: String) {
    viewModelScope.launch(ioDispatcher) {
      _position.value = dbService.getPosition(associationId, positionId)
    }
  }

  fun checkCanDelete(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      DataCache.currentUser.collectLatest { user ->
        _canDelete.value = user.associations.map { it.first }.contains(associationId)
      }
    }
  }

  fun deletePosition(
      associationId: String,
      positionId: String,
      onSuccess: () -> Unit,
      onError: (String) -> Unit
  ) {
    viewModelScope.launch(ioDispatcher) {
      dbService.deletePosition(associationId, positionId, onSuccess, onError)
    }
  }
}
