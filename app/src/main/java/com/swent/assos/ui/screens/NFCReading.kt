package com.swent.assos.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.swent.assos.R
import com.swent.assos.model.view.NFCViewModel

@Composable
fun NFCReading(ticketId: String, context: Activity) {

  val nfcViewModel: NFCViewModel = hiltViewModel()

  val preloaderLottieComposition by
      rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.nfc_animation))

  val preloaderProgress by
      animateLottieCompositionAsState(
          preloaderLottieComposition, iterations = LottieConstants.IterateForever, isPlaying = true)

  Scaffold {
    Column(
        modifier = Modifier.padding(it),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
              text = "Ready to Scan",
              style =
                  TextStyle(
                      fontSize = 30.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      color = Color.Gray.copy(alpha = 0.7f)),
              modifier = Modifier.padding(16.dp))
          LottieAnimation(
              composition = preloaderLottieComposition,
              progress = preloaderProgress,
              modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp))
          Text(
              text = "Hold your device near the NFC tag",
              style =
                  TextStyle(
                      fontSize = 15.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.SemiBold,
                      color = Color.Black),
              modifier = Modifier.padding(16.dp))
          Button(onClick = { context.finish() }, modifier = Modifier.padding(16.dp)) {
            Text("Cancel")
          }
        }
  }
}
