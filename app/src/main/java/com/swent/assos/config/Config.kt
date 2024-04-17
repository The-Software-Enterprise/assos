package com.swent.assos.config

import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Config {
  data class Service(val name: String, val host: String, val port: Int?)

  suspend fun checkFirebaseEmulator(ip: String, port: String): Boolean {
    val url = URL("http://${ip}:${port}")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    var connected = false

    return withContext(Dispatchers.IO) {
      try {
        connection.connect()
        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
          println("Firebase Authentication Emulator is online.")
          connected = true
        } else {
          println(
              "Firebase Authentication Emulator is not yet online. Response code: $responseCode")
        }
      } catch (e: Exception) {
        println("Error checking Firebase Authentication Emulator status: ${e.message}")
      } finally {
        connection.disconnect()
      }
      connected
    }
  }

  fun get_all(): List<String> {
    val onlineServices = mutableListOf<String>()

    try {
      val url = URL("http://10.0.2.2:4400/emulators")
      val connection = url.openConnection() as HttpURLConnection
      connection.requestMethod = "GET"

      connection.connect()
      val responseCode = connection.responseCode
      val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }

      if (responseCode == HttpURLConnection.HTTP_OK) {
        val json = responseMessage.trimIndent()
        val services = JSONObject(json)

        services.keys().forEach { serviceName ->
          val service = services.getJSONObject(serviceName)
          if (service.has("port")) {
            onlineServices.add("$serviceName")
          }
        }
      } else {
        println("Error checking Firebase Emulator status: Response code $responseCode")
      }
    } catch (e: Exception) {
      println("Error checking Firebase Emulator status: ${e.message}")
    }

    return onlineServices
  }

  suspend fun get_demo(): Boolean = checkFirebaseEmulator("10.0.2.2", "9099")
}
