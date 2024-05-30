package com.swent.assos.model.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "associations")
data class Association(
    @PrimaryKey var id: String = "",
    var acronym: String = "",
    var fullname: String = "",
    var url: String = "",
    var documentSnapshot: DocumentSnapshot? = null,
    var logo: Uri = Uri.EMPTY,
    var banner: Uri = Uri.EMPTY,
    var description: String = "",
    @Ignore var openPositions: OpenPositions = OpenPositions()
)
