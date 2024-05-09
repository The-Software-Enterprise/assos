package com.swent.assos

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.IOException

class NFCActivity : Activity() {

  private lateinit var nfcAdapter: NfcAdapter
  private var mode: NFCMode = NFCMode.READ

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // setContentView(R.layout.activity_nfc)

    if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
      // NFC is not supported on this device
      Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
      finish() // Finish the activity
      return
    }

    nfcAdapter = NfcAdapter.getDefaultAdapter(this)
  }

  fun setMode(mode: NFCMode) {
    this.mode = mode
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    when (mode) {
      NFCMode.READ ->
          if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            handleNfcTag(intent)
          }
      NFCMode.WRITE -> {
        val messageWrittenSuccessfully = createNFCMessage("Hello !", intent)
        if (messageWrittenSuccessfully) {
          Toast.makeText(this, "Successfully written to tag", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    enableNFCForegroundDispatch()
  }

  override fun onPause() {
    super.onPause()
    disableNFCForegroundDispatch()
  }

  private fun enableNFCForegroundDispatch() {
    // Enable NFC foreground dispatch to capture NFC intents
    val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    val pendingIntent =
        PendingIntent.getActivity(
            this,
            0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE)
    val intentFilters =
        arrayOf<IntentFilter>(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
    nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
  }

  private fun disableNFCForegroundDispatch() {
    // Disable NFC foreground dispatch
    val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    nfcAdapter.disableForegroundDispatch(this)
  }

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private fun handleNfcTag(intent: Intent) {
    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
    tag?.id?.let {
      val tagValue = it.toHexString()
      Toast.makeText(this, "NFC tag detected: $tagValue", Toast.LENGTH_SHORT).show()
    }
  }

  private fun createNFCIntentFilter(): Array<IntentFilter> {
    val intentFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
    try {
      intentFilter.addDataType("*/*")
    } catch (e: IntentFilter.MalformedMimeTypeException) {
      throw RuntimeException("Failed to add MIME type.", e)
    }
    return arrayOf(intentFilter)
  }

  fun ByteArray.toHexString(): String {
    val hexChars = "0123456789ABCDEF"
    val result = StringBuilder(size * 2)

    map { byte ->
      val value = byte.toInt()
      val hexChar1 = hexChars[value shr 4 and 0x0F]
      val hexChar2 = hexChars[value and 0x0F]
      result.append(hexChar1)
      result.append(hexChar2)
    }

    return result.toString()
  }

  fun createNFCMessage(payload: String, intent: Intent?): Boolean {

    val pathPrefix = "swent.com:nfcapp"
    val nfcRecord =
        NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray())
    val nfcMessage = NdefMessage(arrayOf(nfcRecord))
    intent?.let {
      val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
      return writeMessageToTag(nfcMessage, tag)
    }
    return false
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
    }
    return false
  }
}

enum class NFCMode {
  READ,
  WRITE
}
