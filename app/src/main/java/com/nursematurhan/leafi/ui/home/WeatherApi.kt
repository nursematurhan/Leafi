// WeatherApi.kt
package com.nursematurhan.leafi.data.api

import com.nursematurhan.leafi.ui.home.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
