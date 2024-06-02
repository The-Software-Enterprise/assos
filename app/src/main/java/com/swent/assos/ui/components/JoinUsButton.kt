package com.swent.assos.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R

@Composable
fun JoinUsButton(onClick: () -> Unit, text: String = "Join us") {
  FloatingActionButton(
      onClick = onClick,
      modifier = Modifier.height(42.dp).testTag("JoinButton"),
      elevation = FloatingActionButtonDefaults.elevation(5.dp),
      containerColor = MaterialTheme.colorScheme.primary,
  ) {
    Text(
        modifier = Modifier.padding(horizontal = 10.dp),
        text = text,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.bodyMedium)
  }
}
