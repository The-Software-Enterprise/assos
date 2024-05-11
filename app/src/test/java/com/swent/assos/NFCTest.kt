package com.swent.assos

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Method


@RunWith(AndroidJUnit4::class)
class NFCTest {

  private val mockTag = mockk<Tag>()
  private val mockIntent = mockk<Intent>()
  private val nfcActivity = NFCActivity()

  var tagClass: Class<*> = Tag::class.java
  var createMockTagMethod: Method = tagClass.getMethod(
    "createMockTag",
    ByteArray::class.java,
    IntArray::class.java,
    Array<Bundle>::class.java
  )

  @Test
  fun testReadNFC() {
    every { mockTag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
    every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
    every { mockIntent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) } returns mockTag

    nfcActivity.setMode(NFCMode.READ)

    nfcActivity.onNewIntent(mockIntent)
  }

  @Test
  fun testWriteNFC() {
    every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
    every { mockIntent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) } returns mockTag

    // Mock behavior of NdefFormatable
    val mockNdefFormatable = mockk<NdefFormatable>()
    every { NdefFormatable.get(mockTag) } returns mockNdefFormatable

    nfcActivity.setMode(NFCMode.WRITE)

    nfcActivity.onNewIntent(mockIntent)
    assert(mockTag.boxedValue == "Hello !")
  }
}
