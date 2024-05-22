import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.generateQRCode
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
}
