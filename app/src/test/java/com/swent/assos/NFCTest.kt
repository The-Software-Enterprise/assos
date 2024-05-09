package com.swent.assos

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.core.ValueClassSupport.boxedValue
import io.mockk.every
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Method
import io.mockk.mockk
import org.junit.Test
import java.lang.reflect.Field
import java.lang.reflect.Modifier

interface NfcInteraction {
  fun readTag(): String
  fun writeTag(data: String)
}

class NfcInteractionImpl : NfcInteraction {
  private var data : String? = null

  override fun readTag(): String {
    // Logic to read data from NFC tag
    return "Data from NFC tag"
  }

  override fun writeTag(data: String) {
    // Logic to write data to NFC tag
    this.data = data
  }
}


@RunWith(AndroidJUnit4::class)
class NFCTest {

  private val mockTag = mockk<Tag>()
  private val mockParcelCreator = object : Parcelable.Creator<Tag> {
    override fun createFromParcel(`in`: Parcel): Tag {
      // This closure holds a reference to mockTag.
      return mockTag
    }
    override fun newArray(size: Int): Array<Tag> {
      return arrayOf(mockTag) // Second closure.
    }
  }

  private val intent = mockk<Intent>()
  private val nfcActivity = NFCActivity()

  private fun reassignStaticFinalField(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("accessFlags")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and  Modifier.FINAL.inv())
    field.set(null, newValue)
  }

  @Test
  fun testWriteNFC() {
    reassignStaticFinalField(android.nfc.Tag::class.java.getField("CREATOR"), mockParcelCreator)
    nfcActivity.setMode(NFCMode.WRITE)
    //intent.data = mockTag
    nfcActivity.onNewIntent(intent)
    every { mockTag.boxedValue } returns "Hello !"
  }
}