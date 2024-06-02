package com.swent.assos.model.local_database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.firebase.firestore.DocumentSnapshot
import com.swent.assos.model.data.ParticipationStatus

class Converters {
  @TypeConverter
  fun fromDocumentSnapshot(value: DocumentSnapshot?): String? {
    return null
  }

  @TypeConverter
  fun fromDocumentSnapshot(string: String?): DocumentSnapshot? {
    return null
  }

  @TypeConverter
  fun fromUri(value: Uri?): String? {
    return value.toString()
  }

  @TypeConverter
  fun toUri(string: String?): Uri? {
    return Uri.parse(string)
  }

  @TypeConverter
  fun fromParticipationStatus(value: ParticipationStatus?): String? {
    return value?.name
  }

  @TypeConverter
  fun toParticipationStatus(string: String?): ParticipationStatus? {
    return string?.let { ParticipationStatus.valueOf(it) }
  }
}
