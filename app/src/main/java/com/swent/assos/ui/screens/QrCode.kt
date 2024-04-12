package com.swent.assos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Event
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.StudentSphereTitle
import com.swent.assos.ui.theme.Purple40
import com.swent.assos.ui.theme.Purple80

@Composable
fun QrCode() {
  val eventViewModel: EventViewModel = hiltViewModel()
  val events by eventViewModel.allEvents.collectAsState()
  Scaffold(
    topBar = {
      StudentSphereTitle()
    }) { paddingValues ->
    Surface(color = MaterialTheme.colorScheme.background) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(16.dp),
        userScrollEnabled = true
      ) {
        items(events) {
          TicketItem(event = it)
        }
      }
    }
  }
}

@Composable
fun TicketItem(event: Event) {
  var isExpanded by remember { mutableStateOf(false) } // Keeps track of the expanded state

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
      .clickable { isExpanded = !isExpanded }, // Toggle the expanded state on click
    shape = RoundedCornerShape(8.dp),
    //elevation = 2.dp
  ) {
    Column {
      Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(end = 16.dp)
        ) {
          Text(
            text = "Ticket ${event.title}",
            style = MaterialTheme.typography.titleMedium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = event.date,
            style = MaterialTheme.typography.bodyMedium
          )
        }
        // You can place an icon indicating the expanded state here

      AnimatedVisibility(visible = isExpanded) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = event.description,
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(8.dp))
          Image(
            painter = painterResource(id = R.drawable.logo), // Reference to drawable resource
            contentDescription = "Event Logo",
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp) // Set the desired height for the image
          )
        }
      }
    }
  }
}