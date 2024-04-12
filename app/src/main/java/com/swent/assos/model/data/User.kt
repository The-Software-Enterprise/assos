package com.swent.assos.model.data

data class User(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    var associations: List<String>,
    var following: List<String>
)
