package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateNewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateNewsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreateNewsScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val form: KNode = child { hasTestTag("Form") }
  val inputTitle: KNode =
      form.child {
        hasTestTag("InputTitle")
        hasSetTextAction()
      }
  val inputDescription: KNode =
      form.child {
        hasTestTag("InputDescription")
        hasSetTextAction()
      }
  val showImages: KNode = form.child { hasTestTag("ShowImages") }
  val buttonSave: KNode = form.child { hasTestTag("ButtonSave") }
  val addImages: KNode = onNode { hasTestTag("AddImages") }

  val showImagesDialog: KNode = onNode { hasTestTag("ShowImagesDialog") }
  val imageShown: KNode = showImagesDialog.child { hasTestTag("ImageShown") }

  val addImageDialog: KNode = onNode { hasTestTag("AddImageDialog") }
  val saveImage: KNode = addImageDialog.child { hasTestTag("SaveImage") }
  val cancelImage: KNode = addImageDialog.child { hasTestTag("CancelImage") }
  val inputImage: KNode =
      addImageDialog.child {
        hasTestTag("InputImage")
        hasSetTextAction()
      }
}
