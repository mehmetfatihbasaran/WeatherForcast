package com.example.jetweatherforcast.model_city

data class Components(
    val ISO_3166_1_alpha_2: String,
    val ISO_3166_1_alpha_3: String,
    val ISO_3166_2: List<String>,
    val _category: String,
    val _type: String,
    val city: String,
    val city_district: String,
    val continent: String,
    val country: String,
    val country_code: String,
    val county: String,
    val municipality: String,
    val postcode: String,
    val region: String,
    val road: String,
    val road_type: String,
    val state: String,
    val state_code: String,
    val state_district: String,
    val suburb: String
)