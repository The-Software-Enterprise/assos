package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTitleWithGoBack(title: String, navigationActions: NavigationActions, actionButton: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        title = { Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Normal) },
        navigationIcon = {
            Image(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = null,
                modifier =
                Modifier.testTag("GoBackButton")
                    .padding(start = 16.dp)
                    .clip(RoundedCornerShape(100))
                    .clickable { navigationActions.goBack() }
                    .padding(start = 10.dp, end = 5.dp, top = 7.dp, bottom = 7.dp)
                    .padding()
                    .size(20.dp)
            )
        },
        actions = actionButton,
        colors =
        TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}
