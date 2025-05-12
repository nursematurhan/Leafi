package com.nursematurhan.leafi.ui.home

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Float,
    val humidity: Int
)

data class Weather(
    val main: String,
    val description: String
)
