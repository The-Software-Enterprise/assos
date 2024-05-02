package com.swent.assos.model.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CreateNewsViewModel
@Inject
constructor(private val dbService: DbService, private val accountService: AuthService) :
    ViewModel() {
  private val _news = MutableStateFlow(News())
  var news = _news.asStateFlow()

  private val _showAlertSuccess = MutableStateFlow(false)
  val showAlertSuccess = _showAlertSuccess.asStateFlow()

  private val _showAlertError = MutableStateFlow(false)
  val showAlertError = _showAlertError.asStateFlow()

  fun createNews(associationId: String, navigationActions: NavigationActions) {
    val news = _news.value.copy(associationId = associationId)
    if (news.title.isNotBlank() && news.description.isNotBlank() && news.images.isNotEmpty()) {
      dbService.createNews(news, { navigationActions.goBack() }, {})
    }
  }

  fun updateNews(news: News, navigationActions: NavigationActions) {
    dbService.updateNews(news, {}, {})
  }

  fun deleteNews(news: News, navigationActions: NavigationActions) {
    dbService.deleteNews(news, {}, {})
  }

  fun setTitle(title: String) {
    _news.value = _news.value.copy(title = title)
  }

  fun setDescription(description: String) {
    _news.value = _news.value.copy(description = description)
  }

  fun addImages(images: List<Uri>) {
    _news.value = _news.value.copy(images = _news.value.images + images)
  }

    fun removeImage(image: Uri) {
        _news.value = _news.value.copy(images = _news.value.images - image)
    }
}
