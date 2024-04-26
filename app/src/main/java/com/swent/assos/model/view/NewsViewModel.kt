package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
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
import kotlinx.coroutines.flow.first
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
  private val _news = MutableStateFlow(emptyList<News>())
  val news = _news.asStateFlow()
  private val user = authService.currentUser
  private var userId = ""
  private var _loading = false

  init {
    viewModelScope.launch(ioDispatcher) {
      dbService.getAllNews(null).let { _allNews.value = it }
      userId = user.first().uid
      dbService.filterNewsBasedOnAssociations(null, userId).let { _news.value = it }
    }
  }

  fun getNewsAssociation(associationId: String, callback: (Association) -> Unit) {
    viewModelScope.launch(ioDispatcher) { callback(dbService.getAssociationById(associationId)) }
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
}
