package com.swent.assos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.swent.assos.model.data.Association

@Composable
fun AssociationCard(association: Association, callback: () -> Unit) {
  // TODO : To be changed with Jules.S's implementation
  Card(
      modifier =
          Modifier.fillMaxWidth().clickable { callback() }.padding(8.dp).testTag("AssociationCard"),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
      Text(
          text = association.acronym,
      )
      Text(
          text = association.fullname,
      )
    }
  }
}
