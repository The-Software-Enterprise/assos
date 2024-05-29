package com.swent.assos.ui.screens.assoDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
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
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.DeleteButton
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.EventContent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventDetails(eventId: String, navigationActions: NavigationActions, assoId: String) {

  val eventViewModel: EventViewModel = hiltViewModel()

  val event by eventViewModel.event.collectAsState()
  val assoViewModel: AssoViewModel = hiltViewModel()
  val asso by assoViewModel.association.collectAsState()
  val profileViewModel: ProfileViewModel = hiltViewModel()
  val associations by profileViewModel.memberAssociations.collectAsState()
  val userId by assoViewModel.currentUser.collectAsState()
  var conf by remember { mutableStateOf(false) }

    val isSaved = eventViewModel.isSaved.collectAsState()

  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    assoViewModel.getAssociation(assoId)
    eventViewModel.getEvent(eventId)
    profileViewModel.updateUser()
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("EventDetails"),
      topBar = {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
          PageTitleWithGoBack(title = asso.acronym, navigationActions = navigationActions)

          Image(
              colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
              imageVector = if (isSaved.value)
                  Icons.Default.Bookmark
              else
                  Icons.Default.BookmarkBorder,
              contentDescription = null,
              modifier =
                  Modifier.testTag("SavedIcon")
                      .padding(16.dp)
                      .clip(RoundedCornerShape(100))
                      .clickable {
                        if (isSaved.value) {
                          eventViewModel.unSaveEvent(eventId)
                          Toast.makeText(
                                  context,
                                  "You have successfully removed the event from your saved list",
                                  Toast.LENGTH_SHORT)
                              .show()
                        } else {
                          eventViewModel.saveEvent(eventId)
                          Toast.makeText(
                                  context,
                                  "You have successfully saved the event",
                                  Toast.LENGTH_SHORT)
                              .show()
                        }
                      }
                      .size(30.dp)
                      .align(Alignment.TopEnd))
        }
      },
      floatingActionButton = {
        if (event.id != "") {
          when ((associations.map { it.id }.contains(assoId))) {
            true ->
                Column {
                  Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.Center) {
                        if (event.isStaffingEnabled) {
                          JoinUsButton(
                              onClick = {
                                navigationActions.navigateTo(
                                    Destinations.STAFF_MANAGEMENT.route + "/${eventId}")
                              },
                              text = "Staff List")
                          Spacer(modifier = Modifier.width(10.dp))
                        }
                        JoinUsButton(
                            onClick = {
                              navigationActions.navigateTo(
                                  Destinations.CREATE_TICKET.route + "/${eventId}")
                            },
                            text = "Create ticket")
                      }
                  DeleteButton { conf = true }
                }
            false ->
                if (event.isStaffingEnabled) {
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
          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        if (conf) {
          ConfirmDialog(
              onDismiss = { conf = false },
              onConfirm = {
                conf = false
                eventViewModel.deleteEvent(eventId)
                navigationActions.goBack()
              },
              title = event.title)
        }
        EventContent(
            viewModel = eventViewModel,
            paddingValues = paddingValues,
            isMember = (associations.map { it.id }.contains(assoId)),
            eventId = eventId)
      }
}
