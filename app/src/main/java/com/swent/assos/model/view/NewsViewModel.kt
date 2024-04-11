package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject
constructor(
    private val dbService: DbService
) : ViewModel() {

}