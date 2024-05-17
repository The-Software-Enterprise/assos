package com.swent.assos

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.swent.assos.model.NFC.resolveWritingIntent
import com.swent.assos.model.PendingIntent_Mutable
import com.swent.assos.ui.screens.NFCWriting
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class NFCWriter : ComponentActivity() {

  private var nfcAdapter: NfcAdapter? = null
  private val ticketList = MutableStateFlow(emptyList<String>())
  private lateinit var eventID: String

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    eventID = intent.getStringExtra("eventID") ?: ""

    setContent {
      AssosTheme { NFCWriting(ticketList = ticketList, eventID = eventID, context = this) }
    }

    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    if (nfcAdapter == null) {
      Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      finish() // Finish the activity
      return
    }
  }

  override fun onResume() {
    super.onResume()
    if (nfcAdapter?.isEnabled == false) {
      openNfcSettings()
    }
    val pendingIntent =
      PendingIntent.getActivity(
        this,
        0,
        Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
        PendingIntent_Mutable)
    nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
  }

  override fun onPause() {
    super.onPause()
    nfcAdapter?.disableForegroundDispatch(this)
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  public override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    val finishActivity = resolveWritingIntent(intent, ticketList, this)
    if (finishActivity) {
      finish()
    }
  }

  private fun openNfcSettings() {
    val intent =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Intent(Settings.Panel.ACTION_NFC)
      } else {
        Intent(Settings.ACTION_WIRELESS_SETTINGS)
      }
    startActivity(intent)
  }
}