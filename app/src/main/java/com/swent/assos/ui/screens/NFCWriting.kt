package com.swent.assos.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.view.NFCViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun NFCWriting(ticketList: MutableStateFlow<List<String>>, eventID: String) {
  val nfcViewModel: NFCViewModel = hiltViewModel()

  LaunchedEffect(key1 = Unit) {
    nfcViewModel.collectTickets(eventID = eventID, onSuccess = { ticketList.value = it })
  }
}
