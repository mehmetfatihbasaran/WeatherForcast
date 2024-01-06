package com.example.jetweatherforcast.model_city

data class Result(
    val annotations: Annotations,
    val bounds: Bounds,
    val components: Components,
    val confidence: Int,
    val formatted: String,
    val geometry: Geometry
)