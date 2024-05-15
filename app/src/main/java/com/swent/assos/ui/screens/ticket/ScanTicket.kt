@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitleWithGoBack
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

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
          CameraScreen()
        }
      }
}

@Composable
fun CameraPreview(lifecycleOwner: LifecycleOwner) {
  val context = LocalContext.current
  val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
  val executor = remember { Executors.newSingleThreadExecutor() }
  val imageCapture = remember { ImageCapture.Builder().build() }

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

              cameraProvider.unbindAll()

              try {
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture)
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
fun CameraCaptureButton(imageCapture: ImageCapture, localContext: Context) {
  FloatingActionButton(
      modifier = Modifier.padding(16.dp).testTag("scanButton"),
      onClick = {
        val photoFile =
            File(
                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                    .format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(localContext),
            object : ImageCapture.OnImageSavedCallback {
              override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.d("CameraXApp", "Photo capture succeeded: ${output.savedUri}")
              }

              override fun onError(exc: ImageCaptureException) {
                Log.e("CameraXApp", "Photo capture failed: ${exc.message}", exc)
              }
            })
      },
  ) {
    Text(text = "Scan")
  }
}

@Composable
fun CameraScreen() {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val imageCapture = remember { ImageCapture.Builder().build() }

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

  LaunchedEffect(key1 = true) { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }

  val hasCameraPermission =
      ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
          PackageManager.PERMISSION_GRANTED

  if (hasCameraPermission) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
      CameraPreview(lifecycleOwner = lifecycleOwner)
      CameraCaptureButton(imageCapture = imageCapture, localContext = context)
    }
  }
}
