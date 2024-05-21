package com.swent.assos.model.view

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.swent.assos.model.qr_code.ScannerManager
import com.swent.assos.model.qr_code.ScannerViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *  This class contain the business logic for scanner.
 */
@HiltViewModel
class ScannerViewModel @Inject constructor(): ViewModel(){
  private lateinit var qrCodeManager: ScannerManager

  /**
   * Initialize Camera Manager class.
   */
  internal fun startCamera(
    viewLifecycleOwner: LifecycleOwner,
    context: Context,
    previewView: PreviewView,
    onResult: (state: ScannerViewState, result: String) -> Unit,
  ) {
    qrCodeManager = ScannerManager(
      owner = viewLifecycleOwner, context = context,
      viewPreview = previewView,
      onResult = onResult,
      lensFacing = CameraSelector.LENS_FACING_BACK
    )
  }
}