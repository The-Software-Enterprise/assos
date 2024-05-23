import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.graphics.asImageBitmap
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.generateQRCode
import com.swent.assos.model.saveImageToGallery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRCodeGeneratorTest {

  @Test
  fun testCompareGeneratedQRCodeWithPreExistingQRCode() {
    val content = "event's link"
    val generatedQRCodeBitmap = generateQRCode(content, 500)
    val preExistingQRCodeBitmap = getPreExistingQRCodeBitmap()

    assertTrue(
        "Generated QR code should match the pre-existing QR code",
        compareBitmaps(generatedQRCodeBitmap, preExistingQRCodeBitmap))
  }

  private fun getPreExistingQRCodeBitmap(): Bitmap {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bitmap =
        context.assets.open("pre_existing_qrcode.png").use { BitmapFactory.decodeStream(it) }

    return bitmap!!
  }

  private fun compareBitmaps(bitmap1: Bitmap?, bitmap2: Bitmap?): Boolean {
    if (bitmap1 == null || bitmap2 == null) {
      return false
    }
    if (bitmap1.width != bitmap2.width || bitmap1.height != bitmap2.height) {
      return false
    }
    for (x in 0 until bitmap1.width) {
      for (y in 0 until bitmap1.height) {
        if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
          return false
        }
      }
    }
    return true
  }

  @Test
  fun saveImageToGallery_savesImageSuccessfully() {
    // Mock the dependencies
    val context = mockk<Application>(relaxed = true)
    val contentResolver = mockk<ContentResolver>(relaxed = true)
    val outputStream = mockk<java.io.OutputStream>(relaxed = true)
    // val bitmap = mockk<Bitmap>(relaxed = true)
    val bitmap = generateQRCode("event's link", 500)
    val uri = mockk<Uri>()
    mockkStatic(Toast::class)
    every { Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show() } returns Unit

    // Set up the context and content resolver mocks
    every { context.contentResolver } returns contentResolver
    every { contentResolver.insert(any(), any()) } returns uri
    every { contentResolver.openOutputStream(any()) } returns outputStream
    every { outputStream.close() } returns Unit
    every { contentResolver.update(any(), any(), any()) } returns NUMBER_OF_UPDATED_ROWS
    // Call the function under test
    saveImageToGallery(context, bitmap.asImageBitmap())

    // Verify the interactions
    verify { contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, any()) }
    verify { contentResolver.openOutputStream(uri) }
    verify { bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) }
    verify { outputStream.close() }
    verify { contentResolver.update(uri, any(), null, null) }
    verify { Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show() }
  }

  companion object {
    private const val NUMBER_OF_UPDATED_ROWS = 1
  }
}
