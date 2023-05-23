package com.weather.sunny.http

import com.weather.sunny.bean.Weather36HData
import com.weather.sunny.bean.WeatherForecastForOneWeek
import com.weather.sunny.tool.Tool
import io.reactivex.Observable

object ApiWrapper {

    private fun getApiService():ApiService = RetrofitClient.getRetrofit().create(ApiService::class.java)


    fun getLocationWeather36HoursForecast(location:String) : Observable<Weather36HData> {
        return getApiService().getLocationWeather36HoursForecast(Tool.getApiKey(),location,"JSON","time")
    }

    fun getForecastByLocationForOneWeek(locationKey:String) : Observable<WeatherForecastForOneWeek>{
        return getApiService().getForecastByLocationForTwoDays(locationKey,Tool.getApiKey(),"JSON","MinT,MaxT,PoP12h,T,Wx,WeatherDescription","time")
    }


}