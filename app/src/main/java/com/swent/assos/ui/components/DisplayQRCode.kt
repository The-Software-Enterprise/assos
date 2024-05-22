package com.swent.assos.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.swent.assos.model.generateQRCode

@Composable
fun QRCodeImage(bitmap: Bitmap) {
  Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  MaterialTheme {
    val qrCodeBitmap = generateQRCode("Preview QR Code", 500)
    QRCodeImage(bitmap = qrCodeBitmap)
  }
}
