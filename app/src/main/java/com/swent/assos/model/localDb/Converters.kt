package com.swent.assos.model.localDb

import android.net.Uri
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.DocumentSnapshot

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
}