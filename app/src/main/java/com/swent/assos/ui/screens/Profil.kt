@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swent.assos.R

@Composable
fun Profil() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                actions = {
                    // Ajouter ici l'icône des paramètres
                    Icon(
                        painterResource(id = R.drawable.language),
                        contentDescription = "Map",
                        modifier = Modifier.width(40.dp).height(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 16.dp) ) {

            // Card pour "My associations"
            ProfileCard(title = "My associations", height = 100)
            ProfileCard(title = "Associations I followed", height = 100)
        }
    }
}

@Composable
fun ProfileCard(title: String, height: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(height.dp)

    ) {
        Text(text = title, modifier = Modifier.padding(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Profil()
}