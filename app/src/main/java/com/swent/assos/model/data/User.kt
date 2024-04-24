package com.swent.assos.model.data

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    var associations: List<Triple<String, String, Int>> = emptyList(),
    var following: MutableList<String> = mutableListOf(),
    val sciper: String = "000000",
    val semester: String = "",
)
