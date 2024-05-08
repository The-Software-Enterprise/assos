package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.News
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val authService: AuthService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  private val _allNews = MutableStateFlow(emptyList<News>())
  val allNews = _allNews.asStateFlow()

  private val _news = MutableStateFlow(News())
  val news = _news.asStateFlow()

  private var _loading = false

  init {
    viewModelScope.launch(ioDispatcher) {
      if (DataCache.currentUser.value.id.isNotEmpty()) {
        dbService.filterNewsBasedOnAssociations(null, DataCache.currentUser.value.id).let {
          _allNews.value = it
        }
      } else {
        dbService.getAllNews(null).let { _allNews.value = it }
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

  fun getNews() {
    viewModelScope.launch(ioDispatcher) {
      _allNews.value = dbService.getAllNews(lastDocumentSnapshot = null)
    }
  }
}
