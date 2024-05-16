package com.swent.assos.model

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.firebase.Timestamp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

val MIN_LOADED_ITEMS = 8

enum class AssociationPosition(val string: String, val rank: Int) {
  PRESIDENT("president", 1),
  TREASURER("treasurer", 2),
  MEMBER("member", 3)
}

fun generateQRCodeBitmap(text: String, size: Int): ImageBitmap {
  val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
  val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
  for (x in 0 until size) {
    for (y in 0 until size) {
      bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
    }
  }
  return bitmap.asImageBitmap()
}

fun formatDateTime(date: LocalDateTime): String {
  return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
}

fun generateUniqueID(): String {
  val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  return (1..20).map { chars.random() }.joinToString("")
}

fun timestampToLocalDateTime(timestamp: Timestamp?): LocalDateTime {
  return LocalDateTime.ofInstant(
      Instant.ofEpochSecond(timestamp?.seconds ?: 0), ZoneId.systemDefault())
}

fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Timestamp {
  return Timestamp(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
}

val PendingIntent_Mutable =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      PendingIntent.FLAG_MUTABLE
    } else {
      0
    }

inline fun <reified T> Intent.parcelable(key: String): T? {
  setExtrasClassLoader(T::class.java.classLoader)
  return when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
  }
}
