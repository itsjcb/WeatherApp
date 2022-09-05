package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object{
        const val API_KEY = "5bcdb63153640f230e115f9d6915ad2c"
    }

    @GET("?units=metric&appid=$API_KEY")
    //call is a method that sends a request to a webserver and returns a response.
    fun getWeatherByCity(@Query("q") city: String): Call<WeatherResult>
}