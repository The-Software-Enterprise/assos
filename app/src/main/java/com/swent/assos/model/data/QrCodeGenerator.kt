package com.swent.assos.model.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

fun generateQRCodeBitmap(text: String, size: Int): ImageBitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        text,
        BarcodeFormat.QR_CODE,
        size,
        size
    )
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
        }
    }
    return bitmap.asImageBitmap()
}