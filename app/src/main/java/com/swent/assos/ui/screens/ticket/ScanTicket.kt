@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.qr_code.ScannerAnalyzer
import com.swent.assos.model.qr_code.ScannerViewState
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScanTicket(navigationActions: NavigationActions) {

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("TicketScanScreen"),
      topBar = { PageTitleWithGoBack("Scan a ticket", navigationActions) }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("TicketScanDetails"),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          CameraScreen(navigationActions)
        }
      }
}

@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture,
    onResult: (state: ScannerViewState, result: String) -> Unit
) {
  val context = LocalContext.current
  val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
  val executor = remember { Executors.newSingleThreadExecutor() }

  AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = { ctx ->
        val previewView =
            PreviewView(ctx).apply {
              implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        val preview =
            Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

        cameraProviderFuture.addListener(
            {
              val cameraProvider = cameraProviderFuture.get()
              val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
              // setup analyzer
              val imageAnalysis = ImageAnalysis.Builder().build()
              imageAnalysis.setAnalyzer(
                  ContextCompat.getMainExecutor(ctx),
                  ScannerAnalyzer { state, barcode -> onResult(state, barcode) })

              cameraProvider.unbindAll()

              try {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis)
              } catch (e: Exception) {
                Log.e("CameraPreview", "Binding failed", e)
                print("Binding failed")
              }
            },
            ContextCompat.getMainExecutor(ctx))

        previewView
      })

  DisposableEffect(Unit) { onDispose { executor.shutdown() } }
}

@Composable
fun CameraScreen(navigationActions: NavigationActions) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val imageCapture = remember { ImageCapture.Builder().build() }
  val eventViewModel: EventViewModel = hiltViewModel()
  var scanSucceeded = false

  val cameraPermissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted: Boolean ->
            if (!isGranted) {
              Toast.makeText(
                      context, "Camera permission is required to scan tickets.", Toast.LENGTH_LONG)
                  .show()
            }
          })

  LaunchedEffect(key1 = Unit) { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }

  var hasCameraPermission by remember {
    mutableStateOf(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED)
  }

  if (hasCameraPermission) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
      CameraPreview(
          lifecycleOwner = lifecycleOwner,
          imageCapture = imageCapture,
          onResult = { state, result ->
            when (state) {
              is ScannerViewState.Success -> {
                if (!scanSucceeded) {
                  eventViewModel.createTicket(
                      email = DataCache.currentUser.value.email,
                      eventId = result,
                      onSuccess = {
                        CoroutineScope(Dispatchers.Main).launch {
                          Toast.makeText(context, "Ticket created", Toast.LENGTH_SHORT).show()
                          navigationActions.goBack()
                        }
                      },
                      onFailure = {
                        CoroutineScope(Dispatchers.Main).launch {
                          Toast.makeText(context, "Ticket creation failed", Toast.LENGTH_SHORT)
                              .show()
                          navigationActions.goBack()
                        }
                      },
                      status = ParticipationStatus.Participant)
                }
                scanSucceeded = true
              }
              is ScannerViewState.Error -> {
                Toast.makeText(context, "Barcode scanning failed, try again", Toast.LENGTH_SHORT)
                    .show()
              }
            }
          })
    }
  }
}
