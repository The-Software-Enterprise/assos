package com.swent.assos.ui.screens

import android.content.Intent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.swent.assos.NFCActivity

@Composable
fun Tickets() {
    Text("Tickets")
    val intent = Intent(LocalContext.current, NFCActivity::class.java)
    LocalContext.current.startActivity(intent)
}