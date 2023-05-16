package com.weather.sunny.http

import com.weather.sunny.bean.Weather36HData
import com.weather.sunny.bean.WeatherForecastForOneWeek
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(URLConstant.WEATHER_36_H_FORECAST)
    fun getLocationWeather36HoursForecast(
        @Query("Authorization")token:String,
        @Query("locationName")locationName:String,
        @Query("format")format:String,
        @Query("sort")time:String
    ) : Observable<Weather36HData>

    @GET("v1/rest/datastore/{key}")
    fun getForecastByLocationForTwoDays(
        @Path("key")key:String,
        @Query("Authorization")token:String,
        @Query("format")format:String,
        @Query("elementName")element:String,
        @Query("sort")sort:String
    ) : Observable<WeatherForecastForOneWeek>


}