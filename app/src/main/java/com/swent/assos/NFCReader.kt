package com.swent.assos

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.swent.assos.model.PendingIntent_Mutable
import com.swent.assos.model.parcelable
import com.swent.assos.ui.screens.NFCReading
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class NFCReader : ComponentActivity() {

  private var nfcAdapter: NfcAdapter? = null
  val validIDs = MutableStateFlow(emptyList<String>())
  private lateinit var ticketId: String

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ticketId = intent.getStringExtra("ticketId") ?: ""

    resolveIntent(intent)
    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    if (nfcAdapter == null) {
      // No NFC Dialog
      Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      finish() // Finish the activity
      return
    }

    setContent {
      AssosTheme { NFCReading(ticketList = validIDs, ticketId = ticketId, context = this) }
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
    resolveIntent(intent)
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

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private fun resolveIntent(intent: Intent) {
    val validActions =
        listOf(
            NfcAdapter.ACTION_TAG_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_NDEF_DISCOVERED)
    if (intent.action in validActions) {
      val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
      val messages = mutableListOf<NdefMessage>()

      if (rawMsgs != null) {
        rawMsgs.forEach { messages.add(it as NdefMessage) }
      } else {
        // Unknown tag type
        val empty = ByteArray(0)
        val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        val tag = intent.parcelable<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        val payload = dumpTagData(tag).toByteArray()
        val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
        val msg = NdefMessage(arrayOf(record))
        messages.add(msg)
      }
      messages.forEach {
        if (it.records.size > 1) {
          validIDs.value += String(it.records[1].payload)
        } else {
          validIDs.value += String(it.records[0].payload)
        }
      }
    }
  }

  private fun dumpTagData(tag: Tag): String {
    val sb = StringBuilder()
    val id = tag.id
    sb.append("ID (hex): ").append(toHex(id)).append('\n')
    sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n')
    sb.append("ID (dec): ").append(toDec(id)).append('\n')
    sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n')
    val prefix = "android.nfc.tech."
    sb.append("Technologies: ")
    for (tech in tag.techList) {
      sb.append(tech.substring(prefix.length))
      sb.append(", ")
    }
    sb.delete(sb.length - 2, sb.length)
    for (tech in tag.techList) {
      if (tech == MifareClassic::class.java.name) {
        sb.append('\n')
        var type = "Unknown"
        try {
          val mifareTag = MifareClassic.get(tag)

          when (mifareTag.type) {
            MifareClassic.TYPE_CLASSIC -> type = "Classic"
            MifareClassic.TYPE_PLUS -> type = "Plus"
            MifareClassic.TYPE_PRO -> type = "Pro"
          }
          sb.appendLine("Mifare Classic type: $type")
          sb.appendLine("Mifare size: ${mifareTag.size} bytes")
          sb.appendLine("Mifare sectors: ${mifareTag.sectorCount}")
          sb.appendLine("Mifare blocks: ${mifareTag.blockCount}")
        } catch (e: Exception) {
          sb.appendLine("Mifare classic error: ${e.message}")
        }
      }
      if (tech == MifareUltralight::class.java.name) {
        sb.append('\n')
        val mifareUlTag = MifareUltralight.get(tag)
        var type = "Unknown"
        when (mifareUlTag.type) {
          MifareUltralight.TYPE_ULTRALIGHT -> type = "Ultralight"
          MifareUltralight.TYPE_ULTRALIGHT_C -> type = "Ultralight C"
        }
        sb.append("Mifare Ultralight type: ")
        sb.append(type)
      }
    }
    return sb.toString()
  }

  private fun toHex(bytes: ByteArray): String {
    val sb = StringBuilder()
    for (i in bytes.indices.reversed()) {
      val b = bytes[i].toInt() and 0xff
      if (b < 0x10) sb.append('0')
      sb.append(Integer.toHexString(b))
      if (i > 0) {
        sb.append(" ")
      }
    }
    return sb.toString()
  }

  private fun toReversedHex(bytes: ByteArray): String {
    val sb = StringBuilder()
    for (i in bytes.indices) {
      if (i > 0) {
        sb.append(" ")
      }
      val b = bytes[i].toInt() and 0xff
      if (b < 0x10) sb.append('0')
      sb.append(Integer.toHexString(b))
    }
    return sb.toString()
  }

  private fun toDec(bytes: ByteArray): Long {
    var result: Long = 0
    var factor: Long = 1
    for (i in bytes.indices) {
      val value = bytes[i].toLong() and 0xffL
      result += value * factor
      factor *= 256L
    }
    return result
  }

  private fun toReversedDec(bytes: ByteArray): Long {
    var result: Long = 0
    var factor: Long = 1
    for (i in bytes.indices.reversed()) {
      val value = bytes[i].toLong() and 0xffL
      result += value * factor
      factor *= 256L
    }
    return result
  }
}
