package com.swent.assos.model.data


data class User(
    val uid: Int,
    val firsname: String,
    val lastname: String,
    val email: String,
    var associations: List<Triple<String,String,Int>>,
    var following: List<String>
    )
