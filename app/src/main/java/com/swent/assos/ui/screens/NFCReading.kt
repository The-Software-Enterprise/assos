package com.swent.assos.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.view.NFCViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun NFCReading(ticketList: MutableStateFlow<List<String>>, ticketId: String, context: Activity) {
  val nfcViewModel: NFCViewModel = hiltViewModel()
  val validIDs = ticketList.collectAsState()

  LaunchedEffect(key1 = validIDs) {
    if (nfcViewModel.checkTicket(ticketId, validIDs.value)) {
      Toast.makeText(context, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      context.finish()
    }
  }
}
