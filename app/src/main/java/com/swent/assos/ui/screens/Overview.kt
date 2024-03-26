package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.swent.assos.model.view.OverviewViewModel

@Composable
fun Overview(overviewViewModel: OverviewViewModel) {
  Scaffold {
    Text(
      text = "Hey !",
      modifier = Modifier.padding(it)
    )
  }
}