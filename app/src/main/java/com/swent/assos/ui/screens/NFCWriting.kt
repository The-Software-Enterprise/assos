package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.view.NFCViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun NFCWriting(
    msgListState: MutableStateFlow<List<String>>,
) {
    val nfcViewModel: NFCViewModel = hiltViewModel()
    val msgList = msgListState.collectAsState()

    Scaffold(
        floatingActionButton = {
            AssistChip(
                onClick = {  },
                label = {
                    Text("Writing")
                },
                leadingIcon = {
                    Icon(painter = painterResource(id = R.drawable.edit), contentDescription = null)
                })
        }) {
        Column(modifier = Modifier.padding(it)) {
            msgList.value.forEach {
                Text(it)
                Divider()
            }
        }
    }
}