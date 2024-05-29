package com.swent.assos.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable

@Composable
fun DeleteButton(callBack: () -> Unit) {
  BasicButtonWithIcon(buttonName = "", callback = callBack, icon = Icons.Default.Delete)
}
