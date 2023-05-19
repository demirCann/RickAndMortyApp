package com.demircandemir.rickandmort.model

data class Result(
    val id: Int,
    val name: String,
    val residents: List<String>,
    val url: String,
)