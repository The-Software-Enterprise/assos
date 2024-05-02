package com.swent.assos.model.view

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.News
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateNewsViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val storageService: StorageService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  private val _news = MutableStateFlow(News(id = generateUniqueID()))
  var news = _news.asStateFlow()

  fun createNews(associationId: String, navigationActions: NavigationActions) {
    val news = _news.value.copy(associationId = associationId)
    if (news.title.isNotBlank() && news.description.isNotBlank()) {
      viewModelScope.launch(ioDispatcher) {
        storageService.uploadFiles(
            news.images,
            "news/${news.id}",
            { uris ->
              news.images = uris
              dbService.createNews(news, { navigationActions.goBack() }, {})
            },
            {})
      }
    }
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
