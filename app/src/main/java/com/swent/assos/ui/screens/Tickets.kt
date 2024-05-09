package com.swent.assos.ui.screens

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.swent.assos.NFCActivity

@Composable
fun Tickets() {
    val context = LocalContext.current
    Button(onClick = {
        // Launch NFCActivity
        val intent = Intent(context, NFCActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Launch NFC Activity")
    }
}