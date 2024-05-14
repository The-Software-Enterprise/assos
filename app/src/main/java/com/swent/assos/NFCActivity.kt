package com.swent.assos

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.swent.assos.model.PendingIntent_Mutable
import com.swent.assos.model.parcelable
import com.swent.assos.ui.theme.AssosTheme
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow

class NFCActivity : ComponentActivity() {

  private var nfcAdapter: NfcAdapter? = null
  val _msgList = MutableStateFlow(emptyList<String>())
  private val _mode = MutableStateFlow(NFCMode.READ)

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AssosTheme {
        NFCDisplay(msgListState = _msgList, modeState = _mode, onSwitch = { switchMode() })
      }
    }

    resolveIntent(intent)
    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    if (nfcAdapter == null) {
      // No NFC Dialog
      Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      finish() // Finish the activity
      return
    }
  }

  private fun switchMode() {
    when (_mode.value) {
      NFCMode.READ -> this._mode.value = NFCMode.WRITE
      NFCMode.WRITE -> this._mode.value = NFCMode.READ
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
        when (_mode.value) {
          NFCMode.READ -> {
            rawMsgs.forEach { messages.add(it as NdefMessage) }
          }
          NFCMode.WRITE -> {
            val messageWrittenSuccessfully =
                createNFCMessage("This is a test", intent)
            if (messageWrittenSuccessfully) {
              // Message written to tag
              Toast.makeText(this, "Successfully written to tag", Toast.LENGTH_SHORT).show()
            } else {
              // Something went wrong
              Toast.makeText(this, "Sorry, we could not write your data", Toast.LENGTH_SHORT).show()
            }
          }
        }
      } else {
        // Unknown tag type
        when (_mode.value) {
          NFCMode.READ -> {
            val empty = ByteArray(0)
            val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            val tag = intent.parcelable<Tag>(NfcAdapter.EXTRA_TAG) ?: return
            val payload = dumpTagData(tag).toByteArray()
            val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
            val msg = NdefMessage(arrayOf(record))
            messages.add(msg)
          }
          NFCMode.WRITE -> {
            Toast.makeText(this, "Sorry, we do not know this type of tag", Toast.LENGTH_SHORT)
                .show()
          }
        }
      }

      if (_mode.value == NFCMode.READ) {
        // Setup the views
        messages.forEach {
          if (it.records.size > 1) {
            _msgList.value += "Message: ${String(it.records[1].payload)}"
          } else {
            _msgList.value += "Message: ${String(it.records[0].payload)}"
          }
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

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  fun createNFCMessage(payload: String, intent: Intent): Boolean {
    val pathPrefix = "swent.com:nfcapp"
    val nfcRecord =
        NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray())
    val nfcMessage = NdefMessage(arrayOf(nfcRecord))
    intent.let {
      val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
      return writeMessageToTag(nfcMessage, tag)
    }
  }

  private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

    try {
      val nDefTag = Ndef.get(tag)

      nDefTag?.let {
        it.connect()
        if (it.maxSize < nfcMessage.toByteArray().size) {
          // Message too large to write to NFC tag
          return false
        }
        if (it.isWritable) {
          it.writeNdefMessage(nfcMessage)
          it.close()
          // Message is written to tag
          return true
        } else {
          // NFC tag is read-only
          return false
        }
      }

      val nDefFormatableTag = NdefFormatable.get(tag)

      nDefFormatableTag?.let {
        try {
          it.connect()
          it.format(nfcMessage)
          it.close()
          // The data is written to the tag
          return true
        } catch (e: IOException) {
          // Failed to format tag
          return false
        }
      }
      // NDEF is not supported
      return false
    } catch (e: Exception) {
      // Write operation has failed
      throw e
    }
    return false
  }
}

@Composable
fun NFCDisplay(
    msgListState: MutableStateFlow<List<String>>,
    modeState: MutableStateFlow<NFCMode>,
    onSwitch: () -> Unit
) {

  val msgList = msgListState.collectAsState()
  var mode = modeState.collectAsState()

  Scaffold(
      floatingActionButton = {
        AssistChip(
            onClick = { onSwitch() },
            label = {
              when (mode.value) {
                NFCMode.READ -> Text("Reading")
                NFCMode.WRITE -> Text("Writing")
              }
            },
            leadingIcon = {
              when (mode.value) {
                NFCMode.READ ->
                    Icon(
                        painter = painterResource(id = R.drawable.barcode_reader),
                        contentDescription = null)
                NFCMode.WRITE ->
                    Icon(painter = painterResource(id = R.drawable.edit), contentDescription = null)
              }
            })
      }) {
        Column(modifier = Modifier.padding(it)) {
          msgList.value.forEach {
            Text(it)
            Divider()
          }
        }
      }
}

enum class NFCMode {
  READ,
  WRITE
}
