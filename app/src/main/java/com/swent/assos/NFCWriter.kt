package com.swent.assos

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.swent.assos.model.PendingIntent_Mutable
import com.swent.assos.model.data.Ticket
import com.swent.assos.ui.screens.NFCWriting
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow


@AndroidEntryPoint
class NFCWriter : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    val _msgList = MutableStateFlow(emptyList<String>())
    lateinit var eventId: String

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssosTheme {
                NFCWriting(msgListState = _msgList)
            }
        }

        //start l'activity comme ceci (cf. ChatGPT)
        //val intent = Intent(this, TargetActivity::class.java)
        //intent.putExtra("eventId", value) // "key" is a string identifier, value can be any Serializable or Parcelable object
        //startActivity(intent)
        eventId = intent.getStringExtra("eventId") ?: ""

        resolveIntent(intent)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            // No NFC Dialog
            Toast.makeText(this, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity
            return
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter?.isEnabled == false) {
            openNfcSettings()
        }
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent_Mutable)
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun openNfcSettings() {
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Intent(Settings.Panel.ACTION_NFC)
            } else {
                Intent(Settings.ACTION_WIRELESS_SETTINGS)
            }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun resolveIntent(intent: Intent) {
        val validActions =
            listOf(
                NfcAdapter.ACTION_TAG_DISCOVERED,
                NfcAdapter.ACTION_TECH_DISCOVERED,
                NfcAdapter.ACTION_NDEF_DISCOVERED)
        if (intent.action in validActions) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (rawMsgs != null) {
                        val messageWrittenSuccessfully = createNFCMessage("This is a test", intent)
                        if (messageWrittenSuccessfully) {
                            // Message written to tag
                            Toast.makeText(this, "Successfully written to tag", Toast.LENGTH_SHORT).show()
                        } else {
                            // Something went wrong
                            Toast.makeText(this, "Sorry, we could not write your data", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                // Unknown tag type
                Toast.makeText(this, "Sorry, we do not know this type of tag", Toast.LENGTH_SHORT)
                    .show()

            }
        }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun createNFCMessage(payload: String, intent: Intent): Boolean {
        val pathPrefix = "swent.com:nfcapp"
        val nfcRecord =
            NdefRecord(
                NdefRecord.TNF_EXTERNAL_TYPE,
                pathPrefix.toByteArray(),
                ByteArray(0),
                payload.toByteArray())
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
    }

    private fun createNFCMessageFromTickets(tickets: List<Ticket>): Boolean {
        val pathPrefix = "swent.com:nfcapp"
        val nfcRecord =
            NdefRecord(
                NdefRecord.TNF_EXTERNAL_TYPE,
                pathPrefix.toByteArray(),
                ByteArray(0),
                tickets.map { it.id }.toString().toByteArray())
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    // Message too large to write to NFC tag
                    return false
                }
                if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    // Message is written to tag
                    return true
                } else {
                    // NFC tag is read-only
                    return false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    // The data is written to the tag
                    return true
                } catch (e: IOException) {
                    // Failed to format tag
                    return false
                }
            }
            // NDEF is not supported
            return false
        } catch (e: Exception) {
            // Write operation has failed
            throw e
        }
    }
}