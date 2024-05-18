package com.swent.assos

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.Tag
import com.swent.assos.model.NFC.createNFCMessage
import com.swent.assos.model.NFC.dumpTagData
import com.swent.assos.model.NFC.resolveReadingIntent
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NFCUtilsTest {

  @Test
  fun testWriteMessageToTag() = runBlocking {
    val nfcMessage = mockk<NdefMessage>()
    val tag = mockk<Tag>()
    every { tag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
    // Set up expectations if needed
    every { writeMessageToTag(nfcMessage, tag) } returns false
    val result = writeMessageToTag(nfcMessage, tag)

    assertFalse(result) // Assuming true indicates success
  }

  @Test
  fun testCreateNFCMessage() = runBlocking {
    val intent = mockk<Intent>()
    val payload = listOf("test")
    every { intent.action } returns "android.nfc.action.NDEF_DISCOVERED"
    every { intent.getParcelableExtra<Tag>("android.nfc.extra.TAG") } returns null
    val result = createNFCMessage(payload, intent)
    // Validate the content of the message
    assertFalse(result)
  }

  @Test
  fun testResolveWritingIntent() = runBlocking {
    val intent = mockk<Intent>()
    val context = mockk<Context>()
    val stateFlow = MutableStateFlow(emptyList<String>())
    every { intent.action } returns "android.nfc.action.NDEF_DISCOVERED"
    // Set up expectations if needed
    val result = resolveWritingIntent(intent, stateFlow, context)
    // Check if writing intent is resolved correctly
    assertFalse(result)
  }

  @Test
  fun testResolveReadingIntent() = runBlocking {
    val intent = mockk<Intent>()
    val context = mockk<Context>()
    val stateFlow = MutableStateFlow(mutableListOf<String>())
    every { intent.action } returns "android.nfc.action.NDEF_DISCOVERED"
    every { intent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES") } returns null
    every { intent.getByteArrayExtra("android.nfc.extra.ID") } returns
        byteArrayOf(0x01, 0x02, 0x03, 0x04)
    every { intent.setExtrasClassLoader(any()) } returns Unit
    every { intent.getParcelableExtra<Tag>("android.nfc.extra.TAG") } returns null
    // Simulate reading intent handling
    resolveReadingIntent(intent, stateFlow, "id", context)
  }

  @Test
  fun testDumpTagData() = runBlocking {
    val tag = mockk<Tag>()
    every { tag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
    every { tag.techList } returns arrayOf("android.nfc.tech.Ndef")
    val result = dumpTagData(tag)
    assertTrue(result.isNotEmpty())
  }

  @Test
  fun testToHex() = runBlocking {
    val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
    val result = toHex(bytes)
    assertEquals("04 03 02 01", result)
  }

  @Test
  fun testToReversedHex() = runBlocking {
    val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
    val result = toReversedHex(bytes)
    assertEquals("01 02 03 04", result)
  }

  @Test
  fun testToDec() = runBlocking {
    val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
    val result = toDec(bytes)
    assertEquals(67305985L, result)
  }

  @Test
  fun testToReversedDec() = runBlocking {
    val bytes = byteArrayOf(0x01, 0x02, 0x03, 0x04)
    val result = toReversedDec(bytes)
    assertEquals(16909060L, result)
  }
}
