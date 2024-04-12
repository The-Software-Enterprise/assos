package com.swent.assos.model.data

data class User(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    var associations: List<Triple<String, String, Int>>,
    var following: List<String>
)