package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.AssociationPosition
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  val currentUser = DataCache.currentUser.asStateFlow()

  private val _association = MutableStateFlow(Association())
  val association = _association.asStateFlow()

  private val _associationFollowed = MutableStateFlow(false)
  val associationFollowed = _associationFollowed.asStateFlow()

  private val _news = MutableStateFlow<List<News>>(emptyList())
  val news = _news.asStateFlow()

  private val _events = MutableStateFlow<List<Event>>(emptyList())
  val events = _events.asStateFlow()

  fun getAssociation(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      _association.value = dbService.getAssociationById(associationId)
      _associationFollowed.update {
        DataCache.currentUser.value.following.contains(_association.value.id)
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

  fun joinAssociation(associationId: String) {
    val triple =
        Triple(associationId, AssociationPosition.MEMBER.string, AssociationPosition.MEMBER.rank)
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            associations = DataCache.currentUser.value.associations + triple)
    viewModelScope.launch(ioDispatcher) { dbService.joinAssociation(triple, {}, {}) }
  }
}
