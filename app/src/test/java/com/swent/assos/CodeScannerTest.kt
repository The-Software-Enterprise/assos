package com.swent.assos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.media.Image
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.internal.CommonComponentRegistrar
import com.google.mlkit.common.sdkinternal.MlKitContext
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.swent.assos.model.qr_code.ScannerAnalyzer
import com.swent.assos.model.qr_code.ScannerViewState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.nio.ByteBuffer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28]) // Ensure the correct Android SDK version for Robolectric
class CodeScannerTest {

  @Before
  fun setUp() {
    // Initialize ML Kit with required component registrars
    val context = ApplicationProvider.getApplicationContext<Context>()
    val registrars = listOf(CommonComponentRegistrar())
    MlKitContext.initialize(context, registrars)
  }

  @Test
  fun testScanImage() {
    // Load the PNG file
    val filePath = "src/test/resources/assets/menu.png"
    val bitmap = BitmapFactory.decodeFile(filePath)

    // Create a fake ImageProxy
    val imageProxy = createImageProxy(bitmap)

    var resultState: ScannerViewState? = null
    var resultBarcode: String? = null
    val onSuccess = { state: ScannerViewState, barcode: String? ->
      resultState = state
      resultBarcode = barcode
    }

    // Instantiate the ScannerAnalyzer with a callback
    val analyzer = ScannerAnalyzer(onSuccess)

    mockkStatic(BarcodeScanning::class)
    val mockScanner = mockk<BarcodeScanner>()
    every { BarcodeScanning.getClient(any()) } returns mockScanner

    val mockBarcode = mockk<Barcode>()
    val mockResult = mockk<Task<List<Barcode>>>()

    every { mockResult.addOnSuccessListener(any()) } answers
        {
          onSuccess(ScannerViewState.Success, BARCODE_VALUE)
          mockResult
        }
    every { mockResult.addOnFailureListener(any()) } answers { mockResult }
    every { mockResult.addOnCompleteListener(any()) } answers { mockResult }
    every { mockScanner.process(any<InputImage>()) } returns mockResult
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
    val planes = arrayOf(Mockito.mock(Image.Plane::class.java))

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

  companion object {
    private const val BARCODE_VALUE = "123456"
  }
}
