package com.weather.sunny.tool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.weather.sunny.application.MyApplication
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Tool {


    fun getBaseUrl(): String = "https://opendata.cwb.gov.tw/api/"

    fun getApiKey(): String = "CWB-1510B4D8-CAFC-4B21-8F2E-0F835546D852"

    fun showJson(data : Any):String{
        return Gson().toJson(data)
    }

    fun View.getLocationXY():IntArray{
        val array = IntArray(2)
        this.getLocationInWindow(array)
        return array
    }

    fun Int.getStringExtension():String{
        return getContext().getString(this)
    }

    fun Int.getStringExtension(content1:String,content2:String):String{
        return getContext().getString(this)
    }

    fun Int.convertDp():Int{
        val scale = getContext().resources.displayMetrics.density
        return (this * scale +0.5f).toInt()
    }

    fun isHasGPSPermission():Boolean{
       return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun String.formatTime():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val sdf1 = SimpleDateFormat("MM/dd HHa", Locale.getDefault())
        var date = Date()
        try {
            date = sdf.parse(this) as Date
        }catch (e : Exception){
            e.printStackTrace()
        }
        return sdf1.format(date)
    }

    fun getScreenBottomY(activity:Activity): Int {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = windowManager.currentWindowMetrics
            val windowInsets = metrics.windowInsets

            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )

            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom

// Legacy size that Display#getSize() returns
            val bounds = metrics.bounds
            val legacySize = Size(bounds.width() - insetsWidth, bounds.height() - insetsHeight)
            return legacySize.height
        }
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getColor(resId : Int) : Int {
        return ContextCompat.getColor(getContext(),resId)
    }

    fun getLocationList():MutableList<Pair<String,String>>{
        val list = mutableListOf<Pair<String,String>>()
        list.add(Pair("基隆市","F-D0047-051"))
        list.add(Pair("臺北市","F-D0047-063"))
        list.add(Pair("新北市","F-D0047-071"))
        list.add(Pair("桃園市","F-D0047-007"))
        list.add(Pair("新竹市","F-D0047-055"))
        list.add(Pair("新竹縣","F-D0047-011"))
        list.add(Pair("苗栗縣","F-D0047-015"))
        list.add(Pair("臺中市","F-D0047-075"))
        list.add(Pair("彰化市","F-D0047-019"))
        list.add(Pair("南投縣","F-D0047-023"))
        list.add(Pair("雲林縣","F-D0047-027"))
        list.add(Pair("嘉義市","F-D0047-059"))
        list.add(Pair("嘉義縣","F-D0047-031"))
        list.add(Pair("臺南市","F-D0047-079"))
        list.add(Pair("高雄市","F-D0047-067"))
        list.add(Pair("臺東縣","F-D0047-039"))
        list.add(Pair("花蓮縣","F-D0047-043"))
        list.add(Pair("宜蘭縣","F-D0047-003"))
        list.add(Pair("澎湖縣","F-D0047-047"))
        list.add(Pair("金門縣","F-D0047-087"))
        list.add(Pair("連江縣","F-D0047-083"))
        return list
    }
    private fun getContext():Context{
        return MyApplication.instance?.applicationContext!!
    }
    fun showToast(msg: String) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show()
    }

    fun formatTime(startTime: String?): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var timeMillis : Date? = null
        try {
            startTime?.let {
                timeMillis = sdf.parse(it)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        timeMillis?.let {
            return it.time
        }
        return 0
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(System.currentTimeMillis()))
    }

}