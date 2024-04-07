package com.swent.assos.model.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.model.view.OverviewViewModel
import com.swent.assos.ui.screens.Calendar
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.Overview
import com.swent.assos.ui.screens.Profil
import com.swent.assos.ui.screens.QrCode
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeNavigation(overviewViewModel: OverviewViewModel, navigationActions: NavigationActions) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { 5 }

  Column(
      modifier = Modifier.fillMaxSize(),
  ) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
    ) { page ->
      when (page) {
        0 -> News()
        1 -> Overview(overviewViewModel = overviewViewModel, navigationActions = navigationActions)
        2 -> Calendar()
        3 -> QrCode()
        4 -> Profil()
      }
    }

    NavigationBar(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface),
        containerColor = Color.Transparent,
    ) {
      repeat(5) { index ->
        NavigationBarItem(
            modifier = Modifier.width(30.dp),
            icon = {
              when (index) {
                0 ->
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Overview",
                        tint = MaterialTheme.colorScheme.onSurface)
                1 ->

                    Icon(
                        painterResource(id = R.drawable.language),
                        contentDescription = "Map",
                        tint = MaterialTheme.colorScheme.onSurface)
                2 ->
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Map",
                        tint = MaterialTheme.colorScheme.onSurface)
                3 ->
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Map",
                        tint = MaterialTheme.colorScheme.onSurface)
                4 ->
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Map",
                        tint = MaterialTheme.colorScheme.onSurface)
              }
            },
            selected = pagerState.currentPage == index,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
            colors =
                NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
        )
      }
    }
  }
}
