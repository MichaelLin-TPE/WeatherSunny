package com.weather.sunny.bean

import com.google.gson.annotations.SerializedName

data class WeatherForecastForOneWeek(

    @SerializedName("records")
    val record: WeatherOneWeekRecord? = null

)

data class WeatherOneWeekRecord(
    @SerializedName("locations")
    val locationList: MutableList<WeatherOneWeekLocations>? = null
)

data class WeatherOneWeekLocations(
    @SerializedName("datasetDescription")
    val datasetDescription: String? = null,
    @SerializedName("locationsName")
    val locationsName: String? = null,
    @SerializedName("dataid")
    val dataid: String? = null,
    @SerializedName("location")
    val locationDataList: ArrayList<WeatherOneWeekLocationData>? = null
)

data class WeatherOneWeekLocationData(
    @SerializedName("locationName")
    val locationName: String? = null,
    @SerializedName("geocode")
    val geocode: String? = null,
    @SerializedName("lat")
    val lat: String? = null,
    @SerializedName("lon")
    val lon: String? = null,
    @SerializedName("weatherElement")
    val elementList: ArrayList<WeatherOneWeekElement>? = null
)

data class WeatherOneWeekElement(
    @SerializedName("elementName")
    val elementName: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("time")
    val timeList: ArrayList<WeatherOneWeekTime>? = null
)

data class WeatherOneWeekTime(
    @SerializedName("startTime")
    val startTime: String? = null,
    @SerializedName("endTime")
    val endTime: String? = null,
    @SerializedName("elementValue")
    val elementValueList: ArrayList<WeatherOneWeekElementValue>? = null
)

data class WeatherOneWeekElementValue(
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("measures")
    val measures: String? = null
)