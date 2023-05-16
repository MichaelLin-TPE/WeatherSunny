package com.weather.sunny.bean

import com.google.gson.annotations.SerializedName

data class Weather36HRecords(
    @SerializedName("location")
    val locationList: MutableList<Weather36Location>? = null
)

data class Weather36Location(
    @SerializedName("locationName")
    val locationName: String? = null,
    @SerializedName("weatherElement")
    val weatherElement: MutableList<Weather36WeatherElement>? = null
)

data class Weather36WeatherElement(
    @SerializedName("elementName")
    val elementName: String? = null,
    @SerializedName("time")
    val time: MutableList<Weather36Time>? = null
)

data class Weather36Time(
    @SerializedName("startTime")
    val startTime: String? = null,
    @SerializedName("endTime")
    val endTime: String? = null,
    @SerializedName("parameter")
    val parameter: Weather36Parameter? = null
)

data class Weather36Parameter(
    @SerializedName("parameterName")
    val parameterName: String? = null,
    @SerializedName("parameterValue")
    val parameterValue: String? = null,
    @SerializedName("parameterUnit")
    val parameterUnit: String? = null
)
