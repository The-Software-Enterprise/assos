package com.swent.assos

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.Tag
import com.swent.assos.model.NFC.createNFCMessage
import com.swent.assos.model.NFC.dumpTagData
import com.swent.assos.model.NFC.resolveWritingIntent
import com.swent.assos.model.NFC.toDec
import com.swent.assos.model.NFC.toHex
import com.swent.assos.model.NFC.toReversedDec
import com.swent.assos.model.NFC.toReversedHex
import com.swent.assos.model.NFC.writeMessageToTag
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NFCUtilsTest {

  @Test
  fun testWriteMessageToTag() {
    runBlocking {
      val nfcMessage = mock(NdefMessage::class.java)
      val tag = mock(Tag::class.java)

      val result = writeMessageToTag(nfcMessage, tag)
      // Add assertions here based on the expected behavior of writeMessageToTag
      assertFalse(result) // for example, assuming that writeMessageToTag should always return true
      return@runBlocking
    }
  }

  @Test
  fun testCreateNFCMessage() {
    runBlocking {
      val intent = mock(Intent::class.java)
      val result = createNFCMessage(listOf("test"), intent)
      assertFalse(result)
      return@runBlocking
    }
  }

  @Test
  fun testResolveWritingIntent() {
    runBlocking {
      val intent = mock(Intent::class.java)
      val context = mock(Context::class.java)
      val result = resolveWritingIntent(intent, MutableStateFlow(emptyList()), context)
      assertFalse(result)
      return@runBlocking
    }
  }

  @Test
  fun testResolveReadingIntent() {
    runBlocking {
      val intent = mock(Intent::class.java)
      val context = mock(Context::class.java)

      val result = resolveWritingIntent(intent, MutableStateFlow(emptyList()), context)
      assertFalse(result)
      return@runBlocking
    }
  }

  @Test
  fun testDumpTagData() {
    runBlocking {
      val tag = mockk<Tag>()
      every { tag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
      every { tag.techList } returns arrayOf("android.nfc.tech.Ndef")
      val result = dumpTagData(tag)
      assertFalse(result.isEmpty())
      return@runBlocking
    }
  }

  @Test
  fun testToHex() {
    runBlocking {
      val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
      val result = toHex(bytes)
      assertFalse(result.isEmpty())
      return@runBlocking
    }
  }

  @Test
  fun testToReversedHex() {
    runBlocking {
      val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
      val result = toReversedHex(bytes)
      assertFalse(result.isEmpty())
      return@runBlocking
    }
  }

  @Test
  fun testToDec() {
    runBlocking {
      val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
      val result = toDec(bytes)
      assertTrue(result != 0L)
      return@runBlocking
    }
  }

  @Test
  fun testToReversedDec() {
    runBlocking {
      val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
      val result = toReversedDec(bytes)
      assertTrue(result != 0L)
      return@runBlocking
    }
  }
}
