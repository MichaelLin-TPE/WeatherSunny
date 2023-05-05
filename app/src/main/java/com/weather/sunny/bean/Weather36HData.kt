package com.weather.sunny.bean

import com.google.gson.annotations.SerializedName

data class Weather36HData(
    @SerializedName("records")
    val records:Weather36HRecords? = null
)
