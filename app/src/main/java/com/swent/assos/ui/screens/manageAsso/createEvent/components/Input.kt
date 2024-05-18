package com.swent.assos.ui.screens.manageAsso.createEvent.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    singleLine: Boolean,
    paddingValues: PaddingValues,
    testTag: String,
    enabled: Boolean = true,
) {
  OutlinedTextField(
      modifier = Modifier.fillMaxSize().padding(paddingValues).testTag(testTag),
      value = value,
      onValueChange = onValueChange,
      textStyle =
          TextStyle(
              fontSize = fontSize,
              lineHeight = lineHeight,
              color = MaterialTheme.colorScheme.onBackground),
      placeholder = {
        Text(
            text = placeholder,
            fontSize = fontSize,
            lineHeight = lineHeight,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
      },
      colors =
          OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color.Transparent,
              unfocusedBorderColor = Color.Transparent,
              cursorColor = MaterialTheme.colorScheme.secondary,
              disabledContainerColor = Color.Transparent,
              disabledBorderColor = Color.Transparent,
          ),
      singleLine = singleLine,
      enabled = enabled,
  )
}
