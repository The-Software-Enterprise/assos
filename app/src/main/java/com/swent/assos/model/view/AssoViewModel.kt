package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.News
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

  private val _currentAssociation = MutableStateFlow<Association?>(null)
  val currentAssociation = _currentAssociation.asStateFlow()

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news = _news.asStateFlow()

  fun followAssociation(associationId: String) {
    viewModelScope.launch(ioDispatcher) { dbService.followAssociation(associationId, {}, {}) }
  }

    fun getNews(associationId: String) {
        viewModelScope.launch(ioDispatcher) {
            //_news.value = dbService.getNews(associationId, lastDocumentSnapshot = null)
        }
    }

    fun getMoreNews(associationId: String) {
        viewModelScope.launch(ioDispatcher) {
            val lastDocumentSnapshot = _news.value.lastOrNull()?.documentSnapshot
            //_news.value += dbService.getNews(associationId, lastDocumentSnapshot)
        }
    }
}
