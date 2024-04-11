package com.swent.assos.model.view

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.News
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val dbService: DbService
) : ViewModel() {
    private val _allNews = MutableStateFlow(emptyList<News>())
    val allNews = _allNews.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dbService.getAllNews().let {
                _allNews.value = it
            }
        }
    }

    fun getNewsAssociation(associationId: String, callback: (Association) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            dbService.getAssociationById(associationId).let {
                callback(it)
            }
        }
    }
}