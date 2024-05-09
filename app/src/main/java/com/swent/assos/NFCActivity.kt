package com.swent.assos

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi

class NFCActivity: Activity() {

    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_nfc)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            handleNfcTag(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        enableNFCForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        disableNFCForegroundDispatch()
    }

    private fun enableNFCForegroundDispatch() {
        // Enable NFC foreground dispatch to capture NFC intents
        nfcAdapter.enableForegroundDispatch(this, null, null, null)
    }

    private fun disableNFCForegroundDispatch() {
        // Disable NFC foreground dispatch
        nfcAdapter.disableForegroundDispatch(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleNfcTag(intent: Intent) {
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        tag?.id?.let {
            val tagValue = it.toHexString()
            Toast.makeText(this, "NFC tag detected: $tagValue", Toast.LENGTH_SHORT).show()
        }
    }

    fun ByteArray.toHexString(): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(size * 2)

        map { byte ->
            val value = byte.toInt()
            val hexChar1 = hexChars[value shr 4 and 0x0F]
            val hexChar2 = hexChars[value and 0x0F]
            result.append(hexChar1)
            result.append(hexChar2)
        }

        return result.toString()
    }

}
