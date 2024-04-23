package com.swent.assos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.config.Config
import com.swent.assos.model.navigation.NavigationGraph
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // if we are in debug mode, we want to use the firestore emulator
    val config = Config()
    val firestoreEmu = config.get_all().contains("firestore")
    val authEmu = config.get_all().contains("auth")
    if (firestoreEmu) {
      // Configure Firestore to use the Firestore emulator
      // check if firestore is already using the emulator
      if (FirebaseFirestore.getInstance().firestoreSettings.host != "${R.string.emulatorIP}:8080") {
        FirebaseFirestore.getInstance().useEmulator(R.string.emulatorIP.toString(), 8080)
      }
    }

    if (authEmu) {
      // Configure Firebase Auth to use the Auth emulator
      FirebaseAuth.getInstance().useEmulator(R.string.emulatorIP.toString(), 9099)
    }
    setContent {
      AssosTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          NavigationGraph()
        }
      }
    }
  }
}
