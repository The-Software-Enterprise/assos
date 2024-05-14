package com.swent.assos.model.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class NFCViewModel
@Inject
constructor(
    private val dbService: DbService
) : ViewModel() {

}

