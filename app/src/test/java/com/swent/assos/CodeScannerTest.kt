package com.swent.assos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import androidx.camera.core.ImageProxy
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import android.media.Image
import androidx.camera.core.ImageInfo
import com.swent.assos.model.qr_code.ScannerAnalyzer
import com.swent.assos.model.qr_code.ScannerViewState
import org.mockito.Mockito
import java.nio.ByteBuffer
import java.io.FileInputStream
import java.io.InputStream


@RunWith(RobolectricTestRunner::class)
class CodeScannerTest {
  

  @Test
  fun testScanImage() {
    // Load the PNG file from assets
    val context = ApplicationProvider.getApplicationContext<Context>()
    val assetManager = context.assets
    val inputStream = assetManager.open("menuQRCode.jpg")
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()



    // Create a fake ImageProxy
    val imageProxy = createImageProxy(bitmap)

    var resultState: ScannerViewState? = null
    var resultBarcode: String? = null

    // Instantiate the ScannerAnalyzer with a callback
    val analyzer = ScannerAnalyzer { state, barcode ->
      resultState = state
      resultBarcode = barcode
    }

    // Analyze the image
    analyzer.analyze(imageProxy)

    // Verify the result
    assert(resultState is ScannerViewState.Success)
    assert(resultBarcode != null)
    assert(resultBarcode!!.isNotEmpty())
  }

  private fun createImageProxy(bitmap: Bitmap): ImageProxy {
    // Mock ImageProxy
    val imageProxy = Mockito.mock(ImageProxy::class.java)

    // Mock ImageInfo
    val imageInfo = Mockito.mock(ImageInfo::class.java)
    Mockito.`when`(imageInfo.rotationDegrees).thenReturn(0)
    Mockito.`when`(imageProxy.imageInfo).thenReturn(imageInfo)

    // Mock Image
    val image = Mockito.mock(Image::class.java)
    val planes = arrayOf(
      Mockito.mock(Image.Plane::class.java)
    )

    val buffer = ByteBuffer.allocate(bitmap.byteCount)
    bitmap.copyPixelsToBuffer(buffer)
    buffer.rewind()

    Mockito.`when`(planes[0].buffer).thenReturn(buffer)
    Mockito.`when`(image.planes).thenReturn(planes)
    Mockito.`when`(image.format).thenReturn(ImageFormat.YUV_420_888)

    // Link the mocked image to the ImageProxy
    Mockito.`when`(imageProxy.image).thenReturn(image)
    Mockito.`when`(imageProxy.format).thenReturn(ImageFormat.YUV_420_888)
    Mockito.`when`(imageProxy.width).thenReturn(bitmap.width)
    Mockito.`when`(imageProxy.height).thenReturn(bitmap.height)

    return imageProxy
  }

}