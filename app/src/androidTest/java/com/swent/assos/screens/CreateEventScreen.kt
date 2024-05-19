package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateEventScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateEventScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("CreateEvent") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val createButton: KNode = onNode { hasTestTag("CreateButton") }

  val switch: KNode = onNode { hasTestTag("Switch") }

  val inputTitle: KNode = onNode {
    hasTestTag("InputTitle")
    hasSetTextAction()
  }
  val inputDescription: KNode = onNode {
    hasTestTag("InputDescription")
    hasSetTextAction()
  }

  val inputBanner: KNode = onNode { hasTestTag("InputImage") }
  val startTimePicker: KNode = onNode { hasTestTag("StartTimePicker") }
  val endTimePicker: KNode = onNode { hasTestTag("EndTimePicker") }

  val imageBanner: KNode = onNode { hasTestTag("ImageBanner") }

  val addTextFieldButton: KNode = onNode { hasTestTag("AddTextFieldButton") }
  val addImageFieldButton: KNode = onNode { hasTestTag("AddImageFieldButton") }

  val inputFieldTitle0: KNode = onNode {
    hasTestTag("InputFieldTitle0")
    hasSetTextAction()
  }
  val inputFieldDescription0: KNode = onNode {
    hasTestTag("InputFieldDescription0")
    hasSetTextAction()
  }

  val inputFieldImage1: KNode = onNode { hasTestTag("InputFieldImage1") }
  val listImagesField0: KNode = onNode { hasTestTag("ListImagesField0") }
  val imageListItem: KNode = onNode { hasTestTag("ImageListItem") }
  val deleteImageListItem: KNode = onNode { hasTestTag("DeleteImageListItem") }

  val inputFieldTitle2: KNode = onNode {
    hasTestTag("InputFieldTitle2")
    hasSetTextAction()
  }
  val inputFieldDescription2: KNode = onNode {
    hasTestTag("InputFieldDescription2")
    hasSetTextAction()
  }
}
