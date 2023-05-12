package com.weather.sunny

import android.animation.ObjectAnimator
import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.weather.sunny.base.BaseViewModel
import com.weather.sunny.bean.Weather36Time
import com.weather.sunny.bean.Weather36WeatherElement
import com.weather.sunny.http.ApiWrapper
import com.weather.sunny.tool.Tool
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : BaseViewModel(application) {

    val locationListLiveData = MutableLiveData<MutableList<String>>()
    val showLocationName = MutableLiveData<String>()
    val startAnimationLocationListLiveData = MutableLiveData<Pair<Float, Float>>()
    val showCloudyLiveData = MutableLiveData<Boolean>() // 多雲系列
    val showCloudyAndSunLiveData = MutableLiveData<Boolean>() // 晴時多雲系列
    val showSunLiveData = MutableLiveData<Boolean>() //晴天系列
    val showCurrentTimeLiveData = MutableLiveData<String>()
    val showCloudyAnimationLiveData = MutableLiveData<Int>() // 多雲系列動畫
    val showCloudyAndSunAnimationLiveData = MutableLiveData<Int>() //晴時多雲系列動畫
    val showSunSpinAnimationLiveData = MutableLiveData<Int>() //太陽轉動 動畫


    private var isOpenLocationList = false
    private lateinit var cloudyList : List<Int>
    private lateinit var cloudyAndSunList : List<Int>
    private var viewIndex = 0
    init {
        showLocationName.value = Tool.getLocationList()[0]
        locationListLiveData.value = Tool.getLocationList()
        mCompositeSubscription.add(ApiWrapper.getLocationWeather36HoursForecast(Tool.getLocationList()[0])
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                showErrorMsg(R.string.internet_error.toString())
            }
            .subscribe({

                it.records?.locationList?.let { locationList ->
                    for (location in locationList) {
                        checkWeatherElement(location.weatherElement)
                    }
                }
            }, {
                showErrorMsg(R.string.internet_error.toString())
            })
        )

        showCurrentTime()
    }

    private fun showCurrentTime() {
        mCompositeSubscription.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showCurrentTimeLiveData.value = Tool.getCurrentTime()
                }, {
                    showErrorMsg("時間發生錯誤,請稍後再試")
                })
        )
    }

    private fun checkWeatherElement(weatherElement: MutableList<Weather36WeatherElement>?) {
        weatherElement?.let {
            for (element in it) {
                element.elementName?.let { elementName ->
                    element.time?.let { timeList ->
                        checkTimeAndType(timeList, elementName)
                    }
                }
            }
        }
    }

    private fun checkTimeAndType(timeList: MutableList<Weather36Time>, type: String) {
        var isShowWx = false
        val isFoundWx = type == "Wx"
        for (time in timeList) {
            val startTime = Tool.formatTime(time.startTime)
            val endTime = Tool.formatTime(time.endTime)
            if (time.parameter?.parameterName == null || time.parameter.parameterValue == null) {
                continue
            }
            if (System.currentTimeMillis() in startTime..endTime) {
                when (type) {
                    "Wx" -> {
                        isShowWx = true
                        Log.i("Michael", "找到時間內的資料 : " + time.parameter.parameterName)
                        checkStatus(time.parameter.parameterValue)
                    }

                    "PoP" -> {

                    }

                }
            }
        }
        if (!isShowWx && isFoundWx) {
            checkStatus(timeList[0].parameter?.parameterValue!!)
        }


    }

    private fun checkStatus(parameterValue: String) {
        when (parameterValue) {
            in listOf("4", "5") -> {
                showCloudyLiveData.value = true
            }
            in listOf("2","3") ->{
                showCloudyAndSunLiveData.value = true
            }
            "1"->{
                showSunLiveData.value = true
            }
        }
    }

    private fun getIcon(parameterValue: String): Int {
        when (parameterValue) {
            "1" -> return R.drawable.sun
            "2" -> return R.drawable.sun_and_cloudy
            "3" -> return R.drawable.sun_and_cloudy
            "4" -> return R.drawable.cloudy
            "5" -> return R.drawable.cloudy
        }
        return R.drawable.raining
    }

    fun onSearchBarClickListener() {
        isOpenLocationList = !isOpenLocationList
        startAnimationLocationListLiveData.value =
            if (isOpenLocationList) Pair(0f, 1f) else Pair(1f, 0f)
    }

    fun onCitySelectedListener(locationName: String) {
        showLocationName.value = locationName
        isOpenLocationList = false
        startAnimationLocationListLiveData.value = Pair(1f, 0f)


    }

    fun onDestroy() {
        clearCompositeDisposable()
    }

    fun onStartCloudyAndSunAnimation(){
        cloudyAndSunList = listOf(
            R.id.cloudy1,
            R.id.cloudy2,
            R.id.cloudy3,
            R.id.cloudy4,
            R.id.cloudy5,
            R.id.cloudy6,
            R.id.cloudy7,
            R.id.cloudy8,
            R.id.cloudy9,
            R.id.cloudy10,
            R.id.cloudy11,
            R.id.sun
        )
        startToShowCloudyAndSunAnimation()
    }

    fun onStartSunAnimation(){
        showSunSpinAnimationLiveData.value = R.id.sun
    }

    fun startToShowCloudyAndSunAnimation() {
        if (viewIndex < cloudyAndSunList.size){
            if (cloudyAndSunList[viewIndex] == R.id.sun){
                showSunSpinAnimationLiveData.value = cloudyAndSunList[viewIndex]
            }else{
                showCloudyAndSunAnimationLiveData.value = cloudyAndSunList[viewIndex]
            }
            viewIndex ++
        }else{
            viewIndex = 0;
        }
    }

    fun onStartCloudyAnimation() {
        cloudyList = listOf(
            R.id.cloudy1,
            R.id.cloudy2,
            R.id.cloudy3,
            R.id.cloudy4,
            R.id.cloudy5,
            R.id.cloudy6,
            R.id.cloudy7,
            R.id.cloudy8,
            R.id.cloudy9,
            R.id.cloudy10,
            R.id.cloudy11
        )

        startToShowCloudyAnimation()
    }

    fun startToShowCloudyAnimation() {
        if (viewIndex < cloudyList.size){
            showCloudyAnimationLiveData.value = cloudyList[viewIndex]
            viewIndex ++
        }else{
            viewIndex = 0;
        }
    }


}