package com.swent.assos.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.data.Association

@Composable
fun ListItemAsso(asso: Association, callback: () -> Unit) {
  ListItem(
      modifier =
          Modifier.fillMaxWidth()
              .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
              .background(
                  color = MaterialTheme.colorScheme.onPrimary,
                  shape = RoundedCornerShape(size = 15.dp))
              .testTag("AssoListItem")
              .clickable { callback() },
      headlineContent = {
        Text(
            text = asso.acronym,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface)
      },
      supportingContent = {
        Text(
            text = asso.fullname,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)))
      },
      leadingContent = {
        Image(
            modifier = Modifier.width(56.dp).height(64.dp).clip(shape = RoundedCornerShape(10.dp)),
            painter =
                if (asso.logo == Uri.EMPTY) {
                  painterResource(id = R.drawable.olympics)
                } else {
                  rememberAsyncImagePainter(asso.logo)
                },
            contentDescription = null,
            contentScale = ContentScale.Crop)
      },
      colors = ListItemDefaults.colors(containerColor = Color.Transparent),
  )
}
