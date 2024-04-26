package com.swent.assos

import android.net.Uri
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.swent.assos.model.service.impl.StorageServiceImpl
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StorageServiceTest {

  private lateinit var firebaseStorage: FirebaseStorage
  private lateinit var storageService: StorageServiceImpl
  private lateinit var storageReference: StorageReference

  @Before
  fun setup() {
    firebaseStorage = mockk()
    storageReference = mockk()

    every { firebaseStorage.getReference(any()) } returns storageReference

    mockkStatic(Uri::class)
    val mockUri = mockk<Uri>(relaxed = true)
    every { Uri.parse(any()) } returns mockUri

    val uploadTask: UploadTask = mockk()
    val taskSnapshot: UploadTask.TaskSnapshot = mockk()
    val downloadUrlTask: Task<Uri> = mockk()

    every { storageReference.putFile(mockUri) } returns uploadTask
    every { uploadTask.addOnSuccessListener(any()) } answers
        {
          firstArg<OnSuccessListener<UploadTask.TaskSnapshot>>().onSuccess(taskSnapshot)
          uploadTask
        }
    every { uploadTask.addOnFailureListener(any()) } returns uploadTask

    every { taskSnapshot.storage.downloadUrl } returns downloadUrlTask
    every { downloadUrlTask.addOnSuccessListener(any()) } answers
        {
          firstArg<OnSuccessListener<Uri>>().onSuccess(mockUri)
          downloadUrlTask
        }
    every { downloadUrlTask.addOnFailureListener(any()) } returns downloadUrlTask
    every { mockUri.toString() } returns "https://test.com/file"
  }

  @Test
  fun uploadFilesTest() = runTest {
    val uri1 = Uri.parse("file://dummy/path1")
    val uri2 = Uri.parse("file://dummy/path2")
    val ref = "testRef"

    storageService = StorageServiceImpl(firebaseStorage)
    val result = storageService.uploadFiles(listOf(uri1, uri2), ref)

    assertEquals(listOf("https://test.com/file", "https://test.com/file"), result)
  }
}
