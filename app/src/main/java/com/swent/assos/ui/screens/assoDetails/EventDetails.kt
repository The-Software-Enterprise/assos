package com.swent.assos.ui.screens.assoDetails

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.EventContent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventDetails(eventId: String, navigationActions: NavigationActions, assoId: String) {

  val eventViewModel: EventViewModel = hiltViewModel()

  val event by eventViewModel.event.collectAsState()
  val assoViewModel: AssoViewModel = hiltViewModel()
  val asso by assoViewModel.association.collectAsState()

  val userId by assoViewModel.currentUser.collectAsState()

  val viewModel: ProfileViewModel = hiltViewModel()
  val myAssociations by viewModel.memberAssociations.collectAsState()

  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    assoViewModel.getAssociation(assoId)
    eventViewModel.getEvent(eventId)
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("EventDetails"),
      topBar = { PageTitleWithGoBack(title = asso.acronym, navigationActions = navigationActions) },
      floatingActionButton = {
        if (event.id != "" && !isMember(myAssociations, asso.id) && event.isStaffingEnabled) {
          val applied = eventViewModel.appliedStaff.collectAsState()
          AssistChip(
              colors =
                  if (applied.value)
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.surfaceVariant)
                  else
                      AssistChipDefaults.assistChipColors(
                          containerColor = MaterialTheme.colorScheme.secondary),
              border = null,
              modifier = Modifier.testTag("StaffButton").padding(5.dp),
              onClick = {
                if (applied.value) {
                  eventViewModel.removeRequestToStaff(
                      event.id,
                      userId.id,
                      Toast.makeText(
                              context,
                              "You have successfully removed your request to staff at the event",
                              Toast.LENGTH_SHORT)
                          .show())
                } else {
                  eventViewModel.applyStaffing(
                      event.id,
                      userId.id,
                      Toast.makeText(
                              context,
                              "You have successfully applied to staff at the event",
                              Toast.LENGTH_SHORT)
                          .show())
                }
              },
              label = {
                if (applied.value) {
                  Text(
                      text = "Remove staff application",
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                } else {
                  Text(
                      text = "Apply for staffing",
                      color = MaterialTheme.colorScheme.onSecondary,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.Medium,
                  )
                }
              },
          )
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        EventContent(
            viewModel = eventViewModel,
            paddingValues = paddingValues,
            isMember = isMember(myAssociations = myAssociations, currentAsso = asso.id),
            eventId = eventId,
            navigationActions = navigationActions)
      }
}

private fun isMember(myAssociations: List<Association>, currentAsso: String): Boolean {
  return myAssociations.map { it.id }.contains(currentAsso)
}
