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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swent.assos.R
import com.swent.assos.ui.screens.Explorer
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.Profile
import com.swent.assos.ui.screens.calendar.Calendar
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeNavigation(navigationActions: NavigationActions) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { 4 }

  Column(
      modifier = Modifier.fillMaxSize(),
  ) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = Modifier.fillMaxSize().weight(1f),
    ) { page ->
      when (page) {
        0 -> News()
        1 -> Explorer(navigationActions = navigationActions)
        2 -> Calendar()
        3 -> Profile(navigationActions = navigationActions)
      }
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
        containerColor = Color.Transparent,
    ) {
      repeat(4) { index ->
        NavigationBarItem(
            icon = {
              when (index) {
                0 ->
                    Icon(
                        painterResource(id = R.drawable.house),
                        contentDescription = "Home",
                        modifier = Modifier.size(28.dp).padding(vertical = (2.5).dp),
                        tint =
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                1 ->
                    Icon(
                        painterResource(id = R.drawable.language),
                        contentDescription = "All",
                        modifier = Modifier.size(28.dp),
                        tint =
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                2 ->
                    Icon(
                        painterResource(id = R.drawable.calendar),
                        contentDescription = "Cal",
                        modifier = Modifier.size(28.dp),
                        tint =
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                /*3 ->
                Icon(
                    painterResource(id = R.drawable.qrcode),
                    contentDescription = "QR",
                    modifier = Modifier.size(28.dp).padding(vertical = (2.5).dp),
                    tint =
                        if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))*/
                3 ->
                    Icon(
                        painterResource(id = R.drawable.profil),
                        contentDescription = "Profil",
                        modifier = Modifier.size(28.dp).padding((2.5).dp),
                        tint =
                            if (pagerState.currentPage == index)
                                MaterialTheme.colorScheme.onBackground
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
              }
            },
            selected = pagerState.currentPage == index,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
            colors =
                NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.background,
                ),
        )
      }
    }
  }
}
