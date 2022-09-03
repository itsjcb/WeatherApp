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
import retrofit2.converter.scalars.ScalarsConverterFactory

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

        //TODO: Create retrofit instance
         val retrofit = Retrofit.Builder()
             .baseUrl("https://api.openweathermap.org/data/2.5/weather/")
                 //convert result(json) in string
             .addConverterFactory(GsonConverterFactory.create())
             .build()
        //create the Weather Service
        val weatherService = retrofit.create(WeatherService::class.java)

        //TODO: Call weather API
        val result = weatherService.getWeatherByCity()
        //return apres un service
        result.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val result = response.body()
                    val main = result?.get("main")?.asJsonObject
                    val temp = main?.get("temp")?.asDouble
                    val cityName = result?.get("name")?.asString

                    val weather = result?.get("weather")?.asJsonArray
                    val icon = weather?.get(0)?.asJsonObject?.get("icon")?.asString

                    Picasso.get().load("https://openweathermap/img/w/$icon.png").into(imageWeather)

                    tvTemp.text = "$temp °C"
                    tvCityName.text = cityName

                    layoutWeather.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext,"Server Error", Toast.LENGTH_SHORT).show()
            }

        })
    }
}