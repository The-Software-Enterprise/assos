package com.swent.assos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
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
    if (config.get_demo()) {
      // Configure Firestore to use the Firestore emulator

      // val firestoreSettings = FirebaseFirestoreSettings.Builder()

      // FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
      FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080)
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
