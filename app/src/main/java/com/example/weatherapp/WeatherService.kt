package com.example.weatherapp

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface WeatherService {
    @GET("?q=Montreal&appid=5bcdb63153640f230e115f9d6915ad2c&units=metric")
    //call is a method that sends a request to a webserver and returns a response.
    fun getWeatherByCity(): Call<JsonObject>
}