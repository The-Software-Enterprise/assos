package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePosition(assoId: String, navigationActions: NavigationActions) {
  val assoModel: AssoViewModel = hiltViewModel()
  val association by assoModel.association.collectAsState()
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var requirements by remember { mutableStateOf(listOf<String>()) }
  var responsibilities by remember { mutableStateOf(listOf<String>()) }

  LaunchedEffect(key1 = Unit) { assoModel.getAssociation(assoId) }

  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopAssoBar(asso = association, navigationActions = navigationActions)
        Text(text = "Create Position", modifier = Modifier.padding(8.dp))
      },
      floatingActionButton = {
        SubmitButton {
          assoModel.createPosition(
              assoId, OpenPositions("", title, description, requirements, responsibilities))
          navigationActions.goBack()
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier.fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp) // Add padding here for overall padding
            ) {
              item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier =
                        Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("titleField"),
                    label = { Text("Title") })
              }

              item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("descriptionField"),
                    label = { Text("Description") })
              }

              item {
                DynamicFields(
                    listString = requirements,
                    onAddField = { requirements = requirements + "" },
                    onUpdateField = { index, newValue ->
                      requirements = requirements.toMutableList().apply { set(index, newValue) }
                    },
                    label = "Requirement")
              }

              item {
                DynamicFields(
                    listString = responsibilities,
                    onAddField = { responsibilities = responsibilities + "" },
                    onUpdateField = { index, newValue ->
                      responsibilities =
                          responsibilities.toMutableList().apply { set(index, newValue) }
                    },
                    label = "Responsibility")
              }
            }
      }
}

@Composable
fun DynamicFields(
    listString: List<String>,
    onAddField: () -> Unit,
    onUpdateField: (Int, String) -> Unit,
    label: String,
    paddingValues: PaddingValues = PaddingValues(8.dp)
) {
  Column(modifier = Modifier.fillMaxWidth().padding(paddingValues)) {
    Text(text = label, modifier = Modifier.padding(vertical = 8.dp))
    listString.forEachIndexed { index, item ->
      OutlinedTextField(
          value = item,
          onValueChange = { newValue -> onUpdateField(index, newValue) },
          modifier =
              Modifier.fillMaxWidth()
                  .padding(vertical = 8.dp)
                  .testTag("dynamicField|$label|$index"),
          label = { Text(label) })
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = onAddField,
        modifier =
            Modifier.wrapContentSize(align = Alignment.Center)
                .padding(vertical = 8.dp)
                .testTag("dynamicField|$label|add")) {
          Text("+")
        }
  }
}

@Composable
fun SubmitButton(onClick: () -> Unit) {
  FloatingActionButton(
      onClick = onClick,
  ) {
    Text(text = "Submit")
  }
}
