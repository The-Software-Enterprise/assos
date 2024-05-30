package com.swent.assos.model.data

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    var associations: List<Triple<String, String, Int>> = emptyList(),
    var following: List<String> = mutableListOf(),
    val sciper: String = "000000",
    val semester: String = "",
    var tickets: List<String> = mutableListOf(),
    var appliedAssociation: List<String> = mutableListOf(),
    var appliedStaffing: List<String> = mutableListOf(),
    var savedEvents: List<String> = mutableListOf(),
    var savedNews: List<String> = mutableListOf()
)
