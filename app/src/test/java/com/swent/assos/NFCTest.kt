package com.swent.assos

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import com.swent.assos.model.NFC.createNFCMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NFCTest {

  private val mockTag = mockk<Tag>()
  private val mockIntent = mockk<Intent>()
  private val mockMessage = mockk<NdefMessage>()
  private val mockNDEF = mockk<Ndef>()
  private val readActivity = NFCReader()
  private val writeActivity = NFCWriter()
  private val readPayload: String = "Hello !"
  private val writePayload: String = "Test"

  init {
    readActivity.ticketId = "a2yHSEnKrvdWEClidKEa"
  }

  @Test
  fun testReadNFC() {
    runBlocking {
      every { mockTag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
      every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
      every { mockIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) } returns
          arrayOf(mockMessage)
      val pathPrefix = "swent.com:nfcapp"
      val nfcRecord =
          NdefRecord(
              NdefRecord.TNF_EXTERNAL_TYPE,
              pathPrefix.toByteArray(),
              ByteArray(0),
              readPayload.toByteArray())
      every { mockMessage.records } returns arrayOf(nfcRecord)

      readActivity.onNewIntent(mockIntent)
      val res = readActivity.validIDs.value
      assert(res.contains(readPayload))
      return@runBlocking
    }
  }

  @Test
  fun testWriteNFC() {
    runBlocking {
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

      writeActivity.onNewIntent(mockIntent)
      assert(createNFCMessage(listOf(writePayload), mockIntent))
      val str = String(res.records[0].payload)
      assert(str.contains(writePayload))
      return@runBlocking
    }
  }

  companion object {
    private const val NDEF_MAX_SIZE = 100
  }
}
