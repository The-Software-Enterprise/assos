package com.swent.assos.config

import java.net.HttpURLConnection
import java.net.URL

import org.json.JSONObject

class Config {
  data class Service(val name: String, val host: String, val port: Int?)


  fun checkFirebaseEmulator(ip: String, port: String): Boolean {
    val url = URL("http://${ip}:${port}")
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    var connected: Boolean = false
    var responseCode: Int
    val thread = Thread {
      try {
        connection.connect()
        responseCode = connection.responseCode

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
    }
    thread.start()
    thread.join()
    return connected
  }

  fun get_all(): List<String> {
    var onlineServices: MutableList<String> = mutableListOf<String>()
    var  responseMessage: String = ""
    var responseCode: Int = 0
    val thread1 = Thread {
      // curl localhost:4400/emulators
        val url = URL("http://10.0.2.2:4400/emulators")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
     responseCode = connection.responseCode
     responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
    }




      /* Output:
      {
  "hub": {
    "listen": [
      {
        "address": "127.0.0.1",
        "family": "IPv4",
        "port": 4400
      }
    ],
    "name": "hub",
    "host": "127.0.0.1",
    "port": 4400
  },
  "ui": {
    "listen": [
      {
        "address": "127.0.0.1",
        "family": "IPv4",
        "port": 4000
      }
    ],
    "name": "ui",
    "host": "127.0.0.1",
    "port": 4000,
    "pid": 223714
  },
  "logging": {
    "listen": [
      {
        "address": "127.0.0.1",
        "family": "IPv4",
        "port": 4500
      }
    ],
    "name": "logging",
    "host": "127.0.0.1",
    "port": 4500
  },
  "auth": {
    "listen": [
      {
        "address": "127.0.0.1",
        "family": "IPv4",
        "port": 9099
      }
    ],
    "name": "auth",
    "host": "127.0.0.1",
    "port": 9099
  },
  "functions": {
    "name": "functions",
    "host": "127.0.0.1",
    "port": 5001
  },
  "firestore": {
    "listen": [
      {
        "address": "127.0.0.1",
        "family": "IPv4",
        "port": 8080
      }
    ],
    "name": "firestore",
    "host": "127.0.0.1",
    "port": 8080,
    "pid": 223671,
    "reservedPorts": [
      9150
    ],
    "webSocketHost": "127.0.0.1",
    "webSocketPort": 9150
  },
  "eventarc": {
    "name": "eventarc",
    "host": "127.0.0.1",
    "port": 9299
  }
}*/

    thread1.start()
    thread1.join()

      var json = responseMessage.trimIndent()
      val services = JSONObject(json)

      onlineServices = mutableListOf<String>()
      services.keys().forEach { serviceName ->
        val service = services.getJSONObject(serviceName)
        if (service.has("port")) {
          onlineServices.add("$serviceName")
        }
    }


    return onlineServices
    }

  fun get_demo(): Boolean {
    return checkFirebaseEmulator("10.0.2.2", "9099")
  }
}
