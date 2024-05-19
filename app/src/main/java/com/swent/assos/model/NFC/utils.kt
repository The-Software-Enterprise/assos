package com.swent.assos.model.NFC

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight

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