package com.swent.assos.model.NFC

import android.content.Context
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.swent.assos.model.parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException

fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

  try {
    val nDefTag = Ndef.get(tag)

    nDefTag?.let {
      it.connect()
      if (it.maxSize < nfcMessage.toByteArray().size) {
        // Message too large to write to NFC tag
        it.close()
        return false
      }
      if (it.isWritable) {
        it.writeNdefMessage(nfcMessage)
        it.close()
        // Message is written to tag
        return true
      } else {
        // NFC tag is read-only
        it.close()
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun createNFCMessage(payload: List<String>, intent: Intent): Boolean {
  val pathPrefix = "swent.com:nfcapp"
  val messageList = mutableListOf<NdefRecord>()
  payload.forEach {
    messageList +=
        NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE, pathPrefix.toByteArray(), ByteArray(0), it.toByteArray())
  }

  val nfcMessage = NdefMessage(messageList.toTypedArray())
  intent.let {
    val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
    return writeMessageToTag(nfcMessage, tag)
  }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun resolveWritingIntent(
    intent: Intent,
    ticketList: MutableStateFlow<List<String>>,
    context: Context
): Boolean {
  var finishActivity = false
  val validActions =
      listOf(
          NfcAdapter.ACTION_TAG_DISCOVERED,
          NfcAdapter.ACTION_TECH_DISCOVERED,
          NfcAdapter.ACTION_NDEF_DISCOVERED)
  if (intent.action in validActions) {
    if (ticketList.value.isEmpty()) {
      /* TODO = Manage if an event has no ticket to write*/
      Toast.makeText(
              context, "We could not write your tickets, wait a few seconds...", Toast.LENGTH_SHORT)
          .show()
    } else {
      val messageWrittenSuccessfully = createNFCMessage(ticketList.value, intent)
      if (messageWrittenSuccessfully) {
        // Message written to tag
        Toast.makeText(context, "Successfully written to tag", Toast.LENGTH_SHORT).show()
        finishActivity = true
      } else {
        // Something went wrong
        Toast.makeText(context, "Sorry, we could not write your data", Toast.LENGTH_SHORT).show()
      }
    }
  } else {
    // Unknown tag type
    Toast.makeText(context, "Sorry, we do not know this type of tag", Toast.LENGTH_SHORT).show()
  }
  return finishActivity
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun resolveReadingIntent(
    intent: Intent,
    validIDs: MutableStateFlow<MutableList<String>>,
    ticketId: String,
    context: Context
) {
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
    validIDs.value.clear()
    messages.forEach {
      if (it.records.size > 1) {
        for (i in it.records.indices) {
          if (i != 0) {
            validIDs.value += String(it.records[i].payload)
          }
        }
      } else {
        validIDs.value += String(it.records[0].payload)
      }
    }
    if (validIDs.value.contains(ticketId)) {
      Toast.makeText(context, "You're in !! Have fun", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(context, "Sorry, we can not let you in", Toast.LENGTH_SHORT).show()
    }
  }
}

fun dumpTagData(tag: Tag): String {
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

fun toHex(bytes: ByteArray): String {
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

fun toReversedHex(bytes: ByteArray): String {
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

fun toDec(bytes: ByteArray): Long {
  var result: Long = 0
  var factor: Long = 1
  for (i in bytes.indices) {
    val value = bytes[i].toLong() and 0xffL
    result += value * factor
    factor *= 256L
  }
  return result
}

fun toReversedDec(bytes: ByteArray): Long {
  var result: Long = 0
  var factor: Long = 1
  for (i in bytes.indices.reversed()) {
    val value = bytes[i].toLong() and 0xffL
    result += value * factor
    factor *= 256L
  }
  return result
}
