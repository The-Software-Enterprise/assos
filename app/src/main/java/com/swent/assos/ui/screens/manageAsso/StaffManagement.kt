package com.swent.assos.ui.screens.manageAsso

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.NewsViewModel
import com.swent.assos.ui.components.HomeItem
import com.swent.assos.ui.components.LoadingCircle
import com.swent.assos.ui.components.NameListItem
import com.swent.assos.ui.components.PageTitle
import com.swent.assos.ui.components.PageTitleWithGoBack

@Composable
fun StaffManagement (eventId: String, navigationActions: NavigationActions){

    val eventViewModel: EventViewModel = hiltViewModel()

    val event by eventViewModel.event.collectAsState()
    val listState = rememberLazyListState()


    LaunchedEffect(key1 = Unit) {
        eventViewModel.getEvent(eventId)
    }

    Scaffold(modifier = Modifier.testTag("StaffManagementScreen"), topBar = {
        PageTitleWithGoBack(title = "Staff Management", navigationActions = navigationActions)
    }) {
        paddingValues ->
        LazyColumn(
            modifier =
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 15.dp)
                .padding(vertical = 5.dp)
                .testTag("NewsList"),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            userScrollEnabled = true,
            state = listState) {


                if (event.staff.isEmpty()) {
                    item { Text(text = event.title + stringResource(R.string.NoResult), textAlign = TextAlign.Center) }
                } else {
                    val userIds = event.staff.filterKeys { it == "userId" }.values
                    items(userIds.toList()) { userId -> if (userId is String) { NameListItem(userId = userId, navigationActions = navigationActions)}}
                }

        }
    }

}
