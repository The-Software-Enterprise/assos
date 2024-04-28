package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.Post
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AssoViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  private val _association = MutableStateFlow(Association())
  val association = _association.asStateFlow()

  private val _news = MutableStateFlow<List<News>>(emptyList())
  val news = _news.asStateFlow()

  private val _events = MutableStateFlow<List<Event>>(emptyList())
  val events = _events.asStateFlow()

  fun getAssociation(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      _association.value = dbService.getAssociationById(associationId)
    }
  }

  fun followAssociation(associationId: String) {
    DataCache.currentUser.value.following += associationId
    viewModelScope.launch(ioDispatcher) { dbService.followAssociation(associationId, {}, {}) }
  }

  fun unfollowAssociation(associationId: String) {
    DataCache.currentUser.value.following -= associationId
    viewModelScope.launch(ioDispatcher) { dbService.unfollowAssociation(associationId, {}, {}) }
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
      _events.value = dbService.getEvents(associationId, lastDocumentSnapshot = null)
    }
  }

  fun getMoreEvents(associationId: String) {
    viewModelScope.launch(ioDispatcher) {
      val lastDocumentSnapshot = _events.value.lastOrNull()?.documentSnapshot
      _events.value += dbService.getEvents(associationId, lastDocumentSnapshot)
    }
  }

  fun addPost(post: Post) {
    viewModelScope.launch(ioDispatcher) {
      dbService.addPost(post, {}, {})
    }
  }
}
