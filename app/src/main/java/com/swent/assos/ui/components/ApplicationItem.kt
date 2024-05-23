package com.swent.assos.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.model.data.Applicant

@Composable
fun ApplicationItem(application: Pair<String, Applicant>) {
  Log.d("ApplicationItem", "ApplicationItem: $application")
  Box(
      modifier =
          Modifier.border(0.5.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(6.dp))
              .shadow(4.dp, RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .padding(vertical = 8.dp)
              .padding(horizontal = 16.dp)
              .height(30.dp)
              .testTag("AssociationApplicationItem")) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = application.first,
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(all = 2.dp))
              Text(
                  text = "Status: ${application.second.status}",
                  modifier =
                      Modifier.background(
                          color =
                              if (application.second.status == "accepted")
                                  MaterialTheme.colorScheme.secondary
                              else MaterialTheme.colorScheme.surface,
                          shape = RoundedCornerShape(6.dp)),
              )
            }
      }
}
