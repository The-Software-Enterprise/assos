package com.swent.assos.model

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import java.io.OutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

val MIN_LOADED_ITEMS = 8

enum class AssociationPosition(val string: String, val rank: Int) {
  PRESIDENT("president", 1),
  TREASURER("treasurer", 2),
  MEMBER("member", 3)
}

fun formatDateTime(date: LocalDateTime): String {
  return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
}

fun generateUniqueID(): String {
  val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  return (1..20).map { chars.random() }.joinToString("")
}

fun timestampToLocalDateTime(timestamp: Timestamp?): LocalDateTime {
  return LocalDateTime.ofInstant(
      Instant.ofEpochSecond(timestamp?.seconds ?: 0), ZoneId.systemDefault())
}

fun localDateTimeToTimestamp(localDateTime: LocalDateTime): Timestamp {
  return Timestamp(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
}

val PendingIntent_Mutable =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      PendingIntent.FLAG_MUTABLE
    } else {
      0
    }

inline fun <reified T> Intent.parcelable(key: String): T? {
  setExtrasClassLoader(T::class.java.classLoader)
  return when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
  }
}

fun serialize(event: Event): Map<String, Any> {
  return mapOf(
      "title" to event.title,
      "description" to event.description,
      "associationId" to event.associationId,
      "image" to event.image.toString(),
      "startTime" to localDateTimeToTimestamp(event.startTime),
      "endTime" to localDateTimeToTimestamp(event.endTime),
      "fields" to
          event.fields.map {
            when (it) {
              is Event.Field.Text -> mapOf("type" to "text", "title" to it.title, "text" to it.text)
              is Event.Field.Image ->
                  mapOf("type" to "image", "uris" to it.uris.map { uri -> uri.toString() })
              else -> {}
            }
          },
      "isStaffingEnabled" to event.isStaffingEnabled,
  )
}

fun deserializeEvent(doc: DocumentSnapshot): Event {
  return Event(
      id = doc.id,
      title = doc.getString("title") ?: "",
      description = doc.getString("description") ?: "",
      associationId = doc.getString("associationId") ?: "",
      image = Uri.parse(doc.getString("image") ?: ""),
      startTime = timestampToLocalDateTime(doc.getTimestamp("startTime")),
      endTime = timestampToLocalDateTime(doc.getTimestamp("endTime")),
      fields =
          when (doc["fields"]) {
            is List<*> -> {
              (doc["fields"] as List<*>).mapNotNull { field ->
                when (field) {
                  is Map<*, *> -> {
                    val type = field["type"] as? String
                    when (type) {
                      "text" -> {
                        val title = field["title"] as? String ?: ""
                        val text = field["text"] as? String ?: ""
                        Event.Field.Text(title, text)
                      }
                      "image" -> {
                        val uris =
                            (field["uris"] as? List<*>)?.filterIsInstance<String>()?.map {
                              Uri.parse(it)
                            }
                        when (uris) {
                          null -> null
                          else -> Event.Field.Image(uris)
                        }
                      }
                      else -> null
                    }
                  }
                  else -> null
                }
              }
            }
            else -> emptyList()
          },
      isStaffingEnabled = doc.getBoolean("isStaffingEnabled") ?: false,
      documentSnapshot = doc)
}

fun deserializeTicket(doc: DocumentSnapshot): Ticket {
  return Ticket(
      id = doc.id, eventId = doc.getString("eventId") ?: "", userId = doc.getString("userId") ?: "")
}

fun serialize(news: News): Map<String, Any> {
  return mapOf(
      "title" to news.title,
      "description" to news.description,
      "createdAt" to localDateTimeToTimestamp(news.createdAt),
      "associationId" to news.associationId,
      "images" to news.images,
      "eventId" to news.eventId)
}

fun serialize(applicant: Applicant): Map<String, Any> {
  return mapOf(
      "userId" to applicant.userId,
      "status" to applicant.status,
      "createdAt" to localDateTimeToTimestamp(applicant.createdAt))
}

fun deserializeNews(doc: DocumentSnapshot): News {
  return News(
      id = doc.id,
      title = doc.getString("title") ?: "",
      description = doc.getString("description") ?: "",
      createdAt = timestampToLocalDateTime(doc.getTimestamp("createdAt")),
      associationId = doc.getString("associationId") ?: "",
      images =
          if (doc["images"] is List<*>) {
            (doc["images"] as List<*>).filterIsInstance<String>().toList().map { Uri.parse(it) }
          } else {
            listOf()
          },
      eventId = doc.getString("eventId") ?: "",
      documentSnapshot = doc)
}

fun deserializeAssociation(doc: DocumentSnapshot): Association {
  return Association(
      id = doc.id,
      acronym = doc.getString("acronym") ?: "",
      fullname = doc.getString("fullname") ?: "",
      url = doc.getString("url") ?: "",
      description = doc.getString("description") ?: "",
      logo = doc.getString("logo")?.let { url -> Uri.parse(url) } ?: Uri.EMPTY,
      banner = doc.getString("banner")?.let { url -> Uri.parse(url) } ?: Uri.EMPTY,
      documentSnapshot = doc)
}

fun deserializeApplicant(doc: DocumentSnapshot): Applicant {
  return Applicant(
      id = doc.id,
      userId = doc.getString("userId") ?: "",
      status = doc.getString("status") ?: "unknown",
      createdAt = timestampToLocalDateTime(doc.getTimestamp("createdAt")))
}

fun serialize(user: User): Map<String, Any> {
  return mapOf(
      "firstname" to user.firstName,
      "name" to user.lastName,
      "email" to user.email,
      "following" to user.following,
      "appliedAssociation" to user.appliedAssociation,
      "appliedStaffing" to user.appliedStaffing,
      "tickets" to user.tickets,
      "associations" to
          user.associations.map {
            mapOf("assoId" to it.first, "position" to it.second, "rank" to it.third)
          })
}

fun deserializeUser(doc: DocumentSnapshot): User {
  return User(
      id = doc.id,
      firstName = doc.getString("firstname") ?: "",
      lastName = doc.getString("name") ?: "",
      email = doc.getString("email") ?: "",
      following = (doc.get("following") as? MutableList<String>) ?: mutableListOf(),
      appliedAssociation =
          (doc.get("appliedAssociation") as? MutableList<String>) ?: mutableListOf(),
      appliedStaffing = (doc.get("appliedStaffing") as? MutableList<String>) ?: mutableListOf(),
      associations =
          doc.get("associations")?.let { associations ->
            (associations as? List<Map<String, Any>>)?.mapNotNull {
              val assoId = it["assoId"] as? String
              val position = it["position"] as? String
              val rank = (it["rank"] as? Long)?.toInt()
              if (assoId != null && position != null && rank != null) {
                Triple(assoId, position, rank)
              } else {
                null
              }
            } ?: emptyList()
          } ?: emptyList())
}

fun generateQRCode(text: String, size: Int): Bitmap {
  val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
  val width = bitMatrix.width
  val height = bitMatrix.height
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
  for (x in 0 until width) {
    for (y in 0 until height) {
      bitmap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
    }
  }
  return bitmap
}

fun saveImageToGallery(context: Context, imageBitmap: ImageBitmap) {
  val bitmap = imageBitmap.asAndroidBitmap()
  val contentValues =
      ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "qrcode_${System.currentTimeMillis()}.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
      }

  val resolver = context.contentResolver
  val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

  uri?.let {
    var outputStream: OutputStream? = null

    try {
      outputStream = resolver.openOutputStream(uri)
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      outputStream?.close()
      contentValues.clear()
      contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
      resolver.update(uri, contentValues, null, null)
      Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
    }
  }
}
