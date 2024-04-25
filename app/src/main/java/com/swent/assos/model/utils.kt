package com.swent.assos.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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

fun formatDateTime(dateString: String): String {
  val formatter = DateTimeFormatter.ISO_DATE_TIME
  val dateTime = LocalDateTime.parse(dateString, formatter)
  return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
}

fun generateUniqueID(): String {
  val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  return (1..20).map { chars.random() }.joinToString("")
}
