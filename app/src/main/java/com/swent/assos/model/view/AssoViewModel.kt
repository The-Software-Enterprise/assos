package com.swent.assos.model.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.AssociationPosition
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.CommitteeMember
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AssoViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val storageService: StorageService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  val currentUser = DataCache.currentUser.asStateFlow()

  private val _committee = MutableStateFlow(emptyList<CommitteeMember>())
  val committee = _committee.asStateFlow()

  private val _association = MutableStateFlow(Association())
  val association = _association.asStateFlow()

  private val _associationFollowed = MutableStateFlow(false)
  val associationFollowed = _associationFollowed.asStateFlow()

  private val _news = MutableStateFlow<List<News>>(emptyList())
  val news = _news.asStateFlow()

  private val _events = MutableStateFlow<List<Event>>(emptyList())
  val events = _events.asStateFlow()

  private val _applied = MutableStateFlow(false)
  val applied = _applied.asStateFlow()

  private val _positions = MutableStateFlow<List<OpenPositions>>(emptyList())
  val positions = _positions.asStateFlow()

  fun getCommittee(associationId: String) {
    viewModelScope.launch(ioDispatcher) { _committee.value = dbService.getCommittee(associationId) }
  }

  fun getPositions(associationId: String) {
    viewModelScope.launch(ioDispatcher) { _positions.value = dbService.getPositions(associationId) }
  }

  fun createPosition(associationId: String, position: OpenPositions) {
    viewModelScope.launch(ioDispatcher) { dbService.addPosition(associationId, position) }
  }

  fun getAssociation(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      _association.value = dbService.getAssociationById(associationId)
      _associationFollowed.update {
        DataCache.currentUser.value.following.contains(_association.value.id)
      }
      _applied.update {
        DataCache.currentUser.value.appliedAssociation.contains(_association.value.id)
      }
    }
  }

  fun followAssociation(associationId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            following = DataCache.currentUser.value.following + associationId)
    viewModelScope.launch(ioDispatcher) {
      dbService.followAssociation(associationId, { _associationFollowed.update { true } }, {})
    }
  }

  fun unfollowAssociation(associationId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            following = DataCache.currentUser.value.following.filter { it != associationId })
    viewModelScope.launch(ioDispatcher) {
      dbService.unfollowAssociation(associationId, { _associationFollowed.update { false } }, {})
    }
  }

  fun getNews(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      _news.value = dbService.getNews(associationId, lastDocumentSnapshot = null)
    }
  }

  fun getMoreNews(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      val lastDocumentSnapshot = _news.value.lastOrNull()?.documentSnapshot
      _news.value += dbService.getNews(associationId, lastDocumentSnapshot)
    }
  }

  fun getMorePositions(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      val lastDocumentSnapshot = _positions.value.lastOrNull()?.documentSnapshot
      _positions.value += dbService.getPositions(associationId, lastDocumentSnapshot)
    }
  }

  fun getEvents(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      _events.value =
          dbService.getEventsFromAnAssociation(associationId, lastDocumentSnapshot = null)
    }
  }

  fun getMoreEvents(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      val lastDocumentSnapshot = _events.value.lastOrNull()?.documentSnapshot
      _events.value += dbService.getEventsFromAnAssociation(associationId, lastDocumentSnapshot)
    }
  }

  fun setBanner(banner: Uri?) {
    if (banner != null) {
      viewModelScope.launch(ioDispatcher) {
        val uri = storageService.uploadFile(banner, "associations/${_association.value.id}/banner")
        dbService.updateBanner(_association.value.id, uri)
        _association.value = _association.value.copy(banner = uri)
      }
    }
  }

  fun joinAssociation(associationId: String, userId: String) {
    val triple =
        Triple(associationId, AssociationPosition.MEMBER.string, AssociationPosition.MEMBER.rank)
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            associations = DataCache.currentUser.value.associations + triple)
    viewModelScope.launch(ioDispatcher) { dbService.joinAssociation(triple, userId, {}, {}) }
  }

  fun applyToAssociation(userId: String, successToast: Unit) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            appliedAssociation =
                DataCache.currentUser.value.appliedAssociation + _association.value.id)
    viewModelScope.launch(ioDispatcher) {
      dbService.applyJoinAsso(
          assoId = _association.value.id,
          userId = userId,
          onSuccess = {
            _applied.update { true }
            successToast
          },
          onError = {})
    }
  }

  fun removeRequestToJoin(userId: String, assoId: String, successToast: Unit) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            appliedAssociation =
                DataCache.currentUser.value.appliedAssociation.filter { it != assoId })
    viewModelScope.launch(ioDispatcher) {
      dbService.removeJoinApplication(
          assoId = assoId,
          userId = userId,
          onSuccess = {
            _applied.update { false }
            successToast
          },
          onError = {})
    }
  }

  fun quitAssociation(assoId: String, userId: String) {
    viewModelScope.launch(ioDispatcher) { dbService.quitAssociation(assoId, userId, {}, {}) }
  }
}
