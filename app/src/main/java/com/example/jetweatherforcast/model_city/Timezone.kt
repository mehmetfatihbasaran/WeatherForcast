package com.example.jetweatherforcast.model_city

data class Timezone(
    val name: String,
    val now_in_dst: Int,
    val offset_sec: Int,
    val offset_string: String,
    val short_name: String
)