package com.swent.assos.ui.screens.assoDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.DeleteButton
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.EventContent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventDetails(eventId: String, navigationActions: NavigationActions, assoId: String) {

  val eventViewModel: EventViewModel = hiltViewModel()

  val event by eventViewModel.event.collectAsState()
  val applied = eventViewModel.appliedStaff.collectAsState()
  val assoViewModel: AssoViewModel = hiltViewModel()
  val asso by assoViewModel.association.collectAsState()
  val profileViewModel: ProfileViewModel = hiltViewModel()
  val associations by profileViewModel.memberAssociations.collectAsState()
  val userId by assoViewModel.currentUser.collectAsState()
  var conf by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val loading = profileViewModel.loading.collectAsState()

  val isSaved = eventViewModel.isSaved.collectAsState()

  LaunchedEffect(key1 = Unit) {
    assoViewModel.getAssociation(assoId)
    eventViewModel.getEvent(eventId)
    profileViewModel.updateUser()
  }

  @Composable
  fun labelStaffButton() {
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
  }

  @Composable
  fun staffButtonColors(): ChipColors {
    return if (applied.value)
        AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant)
    else AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.secondary)
  }

  fun callBackStaffRequest() {
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
  }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("EventDetails"),
      topBar = {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
          PageTitleWithGoBack(
              title = {
                Column(
                    modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                      Text(
                          text = event.title,
                          style =
                              TextStyle(
                                  fontSize = 24.sp,
                                  lineHeight = 20.sp,
                                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                  fontWeight = FontWeight.SemiBold,
                                  color = MaterialTheme.colorScheme.onBackground),
                          modifier = Modifier.testTag("PageTitle"))
                      Text(
                          text = asso.acronym,
                          style =
                              TextStyle(
                                  textDecoration = TextDecoration.Underline,
                                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                                  fontWeight = FontWeight.SemiBold,
                                  fontSize = 14.sp,
                                  color = MaterialTheme.colorScheme.onBackground,
                              ),
                          textDecoration = TextDecoration.Underline,
                          modifier =
                              Modifier.clickable {
                                navigationActions.navigateTo(
                                    Destinations.ASSO_DETAILS.route + "/${assoId}")
                              })
                    }
              },
              navigationActions = navigationActions,
              actionButton = {
                Image(
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    imageVector =
                        if (isSaved.value) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
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
                            .size(30.dp))
              })
        }
      },
      floatingActionButton = {
        if (!(associations.map { it.id }.contains(assoId)) &&
            event.isStaffingEnabled &&
            !loading.value) {
          AssistChip(
              colors = staffButtonColors(),
              border = null,
              modifier = Modifier.testTag("StaffButton").padding(5.dp),
              onClick = { callBackStaffRequest() },
              label = { labelStaffButton() },
          )
        }
        DeleteButton { conf = true }
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

        if (loading.value) {
          LoadingCircle()
        } else {
          EventContent(
              viewModel = eventViewModel,
              paddingValues = paddingValues,
              isMember = (associations.map { it.id }.contains(assoId)),
              eventId = eventId,
              navigationActions = navigationActions)
        }
      }
}
