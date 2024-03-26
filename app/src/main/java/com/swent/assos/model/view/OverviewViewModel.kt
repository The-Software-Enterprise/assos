package com.swent.assos.model.view

import androidx.lifecycle.ViewModel
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel
@Inject
constructor(
  private val storageService: DbService,
  private val accountService: AuthService
) : ViewModel() {

}