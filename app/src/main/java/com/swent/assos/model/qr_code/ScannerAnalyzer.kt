package com.swent.assos.model.qr_code

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *  This class scan the codes based on defined format(QR/Barcode) and deliver the result value.
 */
class ScannerAnalyzer(
  private val onResult: (state: ScannerViewState, barcode: String) -> Unit
) : ImageAnalysis.Analyzer {

  private val delayForProcessingNextImage = 300L

  @androidx.annotation.OptIn(ExperimentalGetImage::class)
  override fun analyze(imageProxy: ImageProxy) {
    val options =
      BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
    val scanner = BarcodeScanning.getClient(options)
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
      InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        .let { image ->
          scanner.process(image)
            .addOnSuccessListener { barcodes ->
              for (barcode in barcodes) {
                onResult(ScannerViewState.Success, barcode.rawValue ?: "")
              }
            }
            .addOnFailureListener {
              onResult(ScannerViewState.Error, it.message.toString())
            }
            .addOnCompleteListener {
              CoroutineScope(Dispatchers.IO).launch {
                delay(delayForProcessingNextImage)
                imageProxy.close()
              }
            }
        }
    } else {
      onResult(ScannerViewState.Error, "Image is empty")
    }
  }
}

sealed class ScannerViewState {
  data object Success : ScannerViewState()
  data object Error : ScannerViewState()
}