package com.weather.sunny.tool

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.weather.sunny.application.MyApplication
import java.util.Objects

object Tool {


    fun getBaseUrl(): String = "https://opendata.cwb.gov.tw/api/"

    fun getApiKey(): String = "CWB-1510B4D8-CAFC-4B21-8F2E-0F835546D852"

    fun showJson(data : Any):String{
        return Gson().toJson(data)
    }

    fun getLocationList():MutableList<String>{
        val list = mutableListOf<String>()
        list.add("基隆市")
        list.add("台北市")
        list.add("新北市")
        list.add("桃園縣")
        list.add("新竹市")
        list.add("新竹縣")
        list.add("苗栗縣")
        list.add("台中市")
        list.add("彰化縣")
        list.add("南投縣")
        list.add("雲林縣")
        list.add("嘉義市")
        list.add("嘉義縣")
        list.add("台南市")
        list.add("高雄市")
        list.add("台東縣")
        list.add("花蓮縣")
        list.add("宜蘭縣")
        list.add("澎湖縣")
        list.add("金門縣")
        list.add("連江縣")
        return list
    }
    private fun getContext():Context{
        return MyApplication.instance?.applicationContext!!
    }
    fun showToast(msg: String) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show()
    }

}