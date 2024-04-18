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
      // load data from res/raw/epfl_associations.csv to firestore
      val firestore = FirebaseFirestore.getInstance()
      // check if collection (associations) is empty
      firestore.collection("associations").get().addOnSuccessListener { documents ->
        if (documents.isEmpty) {
          loadAssociations(firestore)
        }
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

  fun loadAssociations(firestore: FirebaseFirestore) {
    val inputStream = resources.openRawResource(R.raw.epfl_associations)
    val reader = inputStream.bufferedReader()
    val lines = reader.readLines()
    for (line in lines) {
      val data = line.split(",")
      val asso =
          hashMapOf(
              "acronym" to data[0],
              "fullname" to data[2],
              "url" to data[1],
          )
      firestore.collection("associations").add(asso)
    }
  }
}
