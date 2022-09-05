package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var editCityName: EditText
    lateinit var btnSearch: Button
    lateinit var imageWeather: ImageView
    lateinit var tvTemp: TextView
    lateinit var tvCityName: TextView

    lateinit var layoutWeather: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editCityName = findViewById(R.id.editCity)
        btnSearch = findViewById(R.id.btnSearch)
        imageWeather = findViewById(R.id.imageWeather)
        tvTemp = findViewById(R.id.tvTemperature)
        tvCityName = findViewById(R.id.txtCityName)
        layoutWeather = findViewById(R.id.layoutWeather)

        btnSearch.setOnClickListener{
            //retrieve the name entered
            val city = editCityName.text.toString()
            if(city.isEmpty()){
                Toast.makeText(this, "City cannot be empty!", Toast.LENGTH_SHORT).show()
            }
            else{
                getWeatherByCity(city)
            }
        }

    }

    private fun getWeatherByCity(city: String) {
        //Retrofit turns your HTTP API into Kotlin Interface
        //TODO1: Create retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
            //convert result(json) in string
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //create the Weather Service
        val weatherService = retrofit.create(WeatherService::class.java)

        //TODO2: Call weather API - WEBSERVICE
        val result = weatherService.getWeatherByCity(city)

        //Asynchronously send the request and notify callback of its response
        result.enqueue(object : Callback<WeatherResult>{
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                if(response.isSuccessful){
                    val result = response.body()

                    tvTemp.text = "${result?.main?.temp} °C"
                    tvCityName.text = result?.name
                    Picasso.get().load("https://openweathermap.org/img/w/${result?.weather?.get(0)?.icon}.png").into(imageWeather)

                    layoutWeather.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                Toast.makeText(applicationContext,"Server Error", Toast.LENGTH_SHORT).show()
            }

        })
    }
}