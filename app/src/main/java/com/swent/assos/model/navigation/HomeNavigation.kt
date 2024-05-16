package com.swent.assos.model.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.ui.screens.Explorer
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.calendar.Calendar
import com.swent.assos.ui.screens.profile.Profile
import com.swent.assos.ui.screens.ticket.MyTickets
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeNavigation(navigationActions: NavigationActions) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { 5 }
  Column(
      modifier =
          Modifier.fillMaxSize().testTag("HomeNavigation").semantics {
            testTagsAsResourceId = true
          },
  ) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier.fillMaxSize().weight(1f).testTag("HorizontalPager"),
    ) { page ->
      when (page) {
        0 -> News(navigationActions = navigationActions)
        1 -> Explorer(navigationActions = navigationActions)
        2 -> Calendar()
        3 -> MyTickets(navigationActions = navigationActions)
        4 -> Profile(navigationActions = navigationActions)
      }
    }
    NavigationBar(
        modifier =
            Modifier.fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .testTag("NavigationBar")
                .semantics { testTagsAsResourceId = true },
        containerColor = Color.Transparent,
    ) {
      repeat(5) { index ->
        NavigationBarItem(
            modifier =
                Modifier.testTag("NavigationBarItem$index").semantics {
                  testTagsAsResourceId = true
                },
            colors =
                NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceTint),
            icon = {
              when (index) {
                0 ->
                    Icon(
                        painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp).padding(bottom = 4.dp),
                        tint = MaterialTheme.colorScheme.onBackground)
                1 ->
                    Icon(
                        painterResource(id = R.drawable.explorer),
                        contentDescription = "Explorer",
                        modifier = Modifier.size(28.dp).padding(bottom = 4.dp),
                        tint = MaterialTheme.colorScheme.onBackground)
                2 ->
                    Icon(
                        painterResource(id = R.drawable.calendar),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(28.dp).padding(bottom = 4.dp),
                        tint = MaterialTheme.colorScheme.onBackground)
                3 ->
                    Icon(
                        painterResource(id = R.drawable.qrcode),
                        contentDescription = "Ticket",
                        modifier =
                            Modifier.size(28.dp).padding(bottom = 4.dp).testTag("TicketIcon"),
                        tint = MaterialTheme.colorScheme.onBackground)
                4 ->
                    Icon(
                        painterResource(id = R.drawable.profile),
                        contentDescription = "Profile",
                        modifier =
                            Modifier.size(28.dp).padding(bottom = 4.dp).testTag("ProfileIcon"),
                        tint = MaterialTheme.colorScheme.onBackground)
              }
            },
            selected = pagerState.currentPage == index,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
            label = {
              val label =
                  when (index) {
                    0 -> "Home"
                    1 -> "Explorer"
                    2 -> "Calendar"
                    3 -> "Ticket"
                    else -> "Profile"
                  }
              if (pagerState.currentPage == index) {
                Text(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground)
              } else {
                Text(text = label, color = MaterialTheme.colorScheme.onBackground)
              }
            })
      }
    }
  }
}
