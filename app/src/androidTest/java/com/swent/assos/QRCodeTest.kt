package com.swent.assos

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.Image
import androidx.camera.core.ImageInfo
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.TagBundle
import androidx.camera.core.impl.utils.ExifData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.mlkit.vision.common.InputImage
import com.swent.assos.model.qr_code.ScannerAnalyzer
import com.swent.assos.model.qr_code.ScannerViewState
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.lang.Thread.sleep
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any

@RunWith(AndroidJUnit4::class)
class CodeScannerTest {

  @Test
  fun testScanImage() {
    // Load the PNG file
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inputStream = context.assets.open("qr_code.png")
    val bitmap = BitmapFactory.decodeStream(inputStream)
    inputStream.close()

    var resultState: ScannerViewState? = null
    var resultBarcode: String? = null
    val onSuccess = { state: ScannerViewState, barcode: String? ->
      resultState = state
      resultBarcode = barcode
    }

    val mockImage = mockk<Image>()
    val mockImageInfo = MockImageInfo()
    val mockImageProxy = AndroidImageProxy(mockImage, mockImageInfo)
    val mockInputImage = mockk<InputImage>()
    mockkStatic(InputImage::class)
    every { InputImage.fromMediaImage(any(), any()) } returns mockInputImage
    every { mockInputImage.width } returns 0
    every { mockInputImage.height } returns 0

    // Instantiate the ScannerAnalyzer with a callback
    val analyzer = ScannerAnalyzer(onSuccess)
    // Analyze the image
    analyzer.analyze(mockImageProxy)
    while (resultBarcode == null) {
      sleep(100)
    }
    // Verify the result
    assert(resultBarcode != null)
    assert(resultState == ScannerViewState.Error)
  }

  companion object {
    private const val BARCODE_VALUE = "123456"
  }

  private class AndroidImageProxy(private val image: Image, private val info: ImageInfo) :
      ImageProxy {

    override fun close() {}

    override fun getCropRect(): Rect {
      return Rect()
    }

    override fun setCropRect(rect: Rect?) {}

    override fun getFormat(): Int {
      return image.format
    }

    override fun getHeight(): Int {
      return image.height
    }

    override fun getWidth(): Int {
      return image.width
    }

    override fun getPlanes(): Array<ImageProxy.PlaneProxy> {
      return arrayOf()
    }

    override fun getImageInfo(): ImageInfo {
      return info
    }

    override fun getImage(): Image {
      return image
    }
  }

  private class CustomImageInfo : ImageInfo {
    override fun getTagBundle(): TagBundle {
      return TagBundle.emptyBundle()
    }

    override fun getTimestamp(): Long {
      return 0
    }

    override fun getRotationDegrees(): Int {
      return 0
    }

    override fun populateExifData(exifBuilder: ExifData.Builder) {}
  }

  private class MockImageInfo : ImageInfo {
    override fun getTagBundle(): TagBundle {
      return TagBundle.emptyBundle()
    }

    override fun getTimestamp(): Long {
      return 0L
    }

    override fun getRotationDegrees(): Int {
      return 0
    }

    override fun populateExifData(exifBuilder: ExifData.Builder) {}
  }
}
