package com.swent.assos.ui.screens.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.swent.assos.R

@Composable
fun Logout(onConfirm: () -> Unit, onDismiss: () -> Unit) {
  AlertDialog(
      onDismissRequest = onDismiss,
      title = {
        Text(
            text = "Log out",
            style =
                TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF1D1B20),
                ))
      },
      containerColor = Color.White,
      text = {
        Text(
            text = "You will be returned to the login screen",
            style =
                TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF49454F),
                    letterSpacing = 0.25.sp,
                ))
      },
      confirmButton = {
        TextButton(onClick = onConfirm) {
          Text(
              text = "Log out",
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(500),
                      color = Color(0xFF6750A4),
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.1.sp,
                  ))
        }
      },
      dismissButton = {
        TextButton(onClick = onDismiss) {
          Text(
              text = "Cancel",
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 20.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight(600),
                      color = Color(0xFF6750A4),
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.1.sp,
                  ))
        }
      })
}
