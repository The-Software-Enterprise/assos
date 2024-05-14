package com.swent.assos

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NFCTest {

  private val mockTag = mockk<Tag>()
  private val mockIntent = mockk<Intent>()
  private val mockMessage = mockk<NdefMessage>()
  private val mockNDEF = mockk<Ndef>()
  private val activity = NFCActivity()

  @Test
  fun testReadNFC() {
    every { mockTag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
    every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
    every { mockIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) } returns
        arrayOf(mockMessage)
    val pathPrefix = "swent.com:nfcapp"
    val payload = "Hello !"
    val nfcRecord =
        NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray())
    every { mockMessage.records } returns arrayOf(nfcRecord)

    activity.onNewIntent(mockIntent)
    val res = activity._msgList.value
    assert(res.contains("Message: $payload"))
  }

  @Test
  fun testWriteNFC() {
    every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
    every { mockIntent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) } returns mockTag
    mockkStatic(Ndef::class)
    every { Ndef.get(mockTag) } returns mockNDEF
    every { mockNDEF.maxSize } returns NDEF_MAX_SIZE
    every { mockNDEF.connect() } returns Unit
    every { mockNDEF.isWritable } returns true
    var res = mockMessage
    every { mockNDEF.writeNdefMessage(any()) } answers
        { msg ->
          res = msg.invocation.args[0] as NdefMessage
        }
    every { mockNDEF.close() } returns Unit

    assert(activity.createNFCMessage("Test", mockIntent))
    val str = String(res.records[0].payload)
    assert(str.contains("Test"))
  }

  companion object {
    private const val NDEF_MAX_SIZE = 100
  }
}
