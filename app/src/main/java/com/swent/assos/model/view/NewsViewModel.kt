package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.News
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val dbService: DbService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  private val _allNews = MutableStateFlow(emptyList<News>())
  val allNews = _allNews.asStateFlow()

  private val _allNewsOfAllAssos = MutableStateFlow(emptyList<News>())
  val allNewsOfAllAssos = _allNewsOfAllAssos.asStateFlow()

  private val _news = MutableStateFlow(News())
  val news = _news.asStateFlow()

  private var _loading = false

  private var _loadingDisplay = MutableStateFlow(true)
  val loading = _loadingDisplay.asStateFlow()

  private val _isSaved = MutableStateFlow(false)
  val isSaved = _isSaved.asStateFlow()

  fun loadNews(newsId: String) {
    viewModelScope.launch(ioDispatcher) {
      dbService.getNews(newsId).let {
        _news.value = it
        _loadingDisplay.value = false
      }
      _isSaved.update { DataCache.currentUser.value.savedNews.contains(_news.value.id) }
    }
  }

  fun deleteNews(newsId: String) {
    viewModelScope.launch(ioDispatcher) { dbService.deleteNews(newsId) }
  }

  fun loadNews() {
    viewModelScope.launch(ioDispatcher) {
      if (DataCache.currentUser.value.id.isNotEmpty()) {
        dbService.filterNewsBasedOnAssociations(null, DataCache.currentUser.value.id).let {
          _allNews.value = it
          _loadingDisplay.value = false
        }
      } else {
        dbService.getAllNews(null).let {
          _allNews.value = it
          _loadingDisplay.value = false
        }
      }
    }
  }

  fun loadAllNews() {
    viewModelScope.launch(ioDispatcher) {
      dbService.getAllNews(null).let {
        _allNewsOfAllAssos.value = it
        _loadingDisplay.value = false
      }
    }
  }

  fun loadMoreAssociations() {
    if (!_loading) {
      _loading = true
      viewModelScope.launch(Dispatchers.IO) {
        val lastDocumentSnapshot = _allNews.value.lastOrNull()?.documentSnapshot
        dbService.getAllNews(lastDocumentSnapshot).let {
          _allNews.value += it
          _allNews.value = _allNews.value.distinct()
          if (it.isNotEmpty()) {
            _loading = false
          }
        }
      }
    }
  }

  fun saveNews(newsId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(savedNews = DataCache.currentUser.value.savedNews + newsId)
    viewModelScope.launch(ioDispatcher) {
      dbService.saveNews(newsId, { _isSaved.update { true } }, {})
    }
  }

  fun unSaveNews(newsId: String) {
    DataCache.currentUser.value =
        DataCache.currentUser.value.copy(
            savedNews = DataCache.currentUser.value.savedNews.filter { it != newsId })
    viewModelScope.launch(ioDispatcher) {
      dbService.unSaveNews(newsId, { _isSaved.update { false } }, {})
    }
  }
}
