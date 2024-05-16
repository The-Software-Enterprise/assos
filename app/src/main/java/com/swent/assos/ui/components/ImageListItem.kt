package com.swent.assos.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageListItem(uri: Uri, onDelete: (() -> Unit)? = null) {
  Box(
      modifier = Modifier.fillMaxHeight(0.9f).aspectRatio(1f).clip(RoundedCornerShape(8.dp)),
  ) {
    Image(
        painter = rememberAsyncImagePainter(uri),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize().align(Alignment.Center).testTag("ImageListItem"),
    )
    if (onDelete != null) {
      Image(
          imageVector = Icons.Default.DeleteForever,
          contentDescription = "Trash",
          modifier =
              Modifier.align(Alignment.TopEnd)
                  .padding(6.dp)
                  .size(30.dp)
                  .background(
                      MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                      RoundedCornerShape(5.dp))
                  .clickable { onDelete() }
                  .padding(3.dp)
                  .testTag("DeleteImageListItem"),
          colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)),
      )
    }
  }
}
