package com.swent.assos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.impl.AuthServiceImpl
import com.swent.assos.model.service.impl.DbServiceImpl
import com.swent.assos.model.view.OverviewViewModel
import com.swent.assos.ui.screens.Overview
import com.swent.assos.ui.theme.AssosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val overviewViewModel: OverviewViewModel = hiltViewModel()
                    Overview(overviewViewModel)
                }
            }
        }
    }
}
