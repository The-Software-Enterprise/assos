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
import com.swent.assos.model.NFC.resolveReadingIntent
import com.swent.assos.model.PendingIntent_Mutable
import com.swent.assos.ui.screens.NFCReading
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class NFCReader : ComponentActivity() {

  private var nfcAdapter: NfcAdapter? = null
  val validIDs = MutableStateFlow(mutableListOf<String>())
  lateinit var ticketId: String

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ticketId = intent.getStringExtra("ticketId") ?: ""

    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    if (nfcAdapter == null) {
      // No NFC Dialog
      Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      finish() // Finish the activity
      return
    }

    setContent { AssosTheme { NFCReading(ticketId = ticketId, context = this) } }
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
    resolveReadingIntent(intent, validIDs, ticketId, this)
    finish()
  }
}
