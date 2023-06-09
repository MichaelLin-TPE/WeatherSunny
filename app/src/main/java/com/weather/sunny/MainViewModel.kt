package com.weather.sunny

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.weather.sunny.base.BaseViewModel
import com.weather.sunny.bean.Weather36Time
import com.weather.sunny.bean.Weather36WeatherElement
import com.weather.sunny.bean.WeatherOneWeekData
import com.weather.sunny.bean.WeatherOneWeekElement
import com.weather.sunny.bean.WeatherOneWeekLocationData
import com.weather.sunny.bean.WeatherOneWeekLocations
import com.weather.sunny.bean.WeatherOneWeekTime
import com.weather.sunny.http.ApiWrapper
import com.weather.sunny.tool.Tool
import com.weather.sunny.tool.Tool.formatTime
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : BaseViewModel(application) {

    val locationListLiveData = MutableLiveData<MutableList<Pair<String, String>>>()
    val showLocationName = MutableLiveData<String>()
    val showSecondLocationName = MutableLiveData<String>()
    val startAnimationLocationListLiveData = MutableLiveData<Pair<Float, Float>>()
    val startAnimationSecondLocationListLiveData = MutableLiveData<Pair<Float, Float>>()
    val showCloudyLiveData = MutableLiveData<Boolean>() //多雲系列
    val showCloudyAndSunLiveData = MutableLiveData<Boolean>() //晴時多雲系列
    val showSunLiveData = MutableLiveData<Boolean>() //晴天系列
    val showCloudyAndRainLiveData = MutableLiveData<Boolean>() //多雲有雨系列
    val showCloudyDarkAndRainLiveData = MutableLiveData<Boolean>() //陰且有雨系列
    val showCloudyDarkAndRainWithThunderLiveData = MutableLiveData<Boolean>() //雷雨系列
    val showCloudyAndCloudyDarkLiveData = MutableLiveData<Boolean>() //多雲時陰系列
    val showCloudyDarkLiveData = MutableLiveData<Boolean>()
    val showCurrentTimeLiveData = MutableLiveData<String>()
    val showCloudyAnimationLiveData = MutableLiveData<Int>() // 多雲系列動畫
    val showCloudyAndSunAnimationLiveData = MutableLiveData<Int>() //晴時多雲系列動畫
    val showSunSpinAnimationLiveData = MutableLiveData<Int>() //太陽轉動 動畫
    val showRainLiveData = MutableLiveData<Pair<Float, Float>>() // 顯示下雨
    val showThunderLiveData = MutableLiveData<Pair<Float, Float>>() //顯示打雷
    val showPopPercentLiveData = MutableLiveData<String>() // 顯示降雨機率
    val showCiInformationLivedata = MutableLiveData<String>()
    val showTempLiveData = MutableLiveData<String>()
    val showOneWeekForecastLiveData = MutableLiveData<MutableList<WeatherOneWeekData>>()
    val showLocationList = MutableLiveData<MutableList<String>>()
    val requestGPSPermissionLiveData = MutableLiveData<Boolean>()
    val searchLocationLiveData = MutableLiveData<Boolean>()
    private var rainingDisposable: Disposable? = null
    private var thunderDisposable: Disposable? = null
    private var targetSecondLocationName = ""

    private var isOpenLocationList = false
    private var isOpenSecondLocationList = false
    private lateinit var cloudyList: List<Int>
    private lateinit var cloudyAndSunList: List<Int>
    private var viewIndex = 0
    private var minT = ""
    private var maxT = ""
    private val oneWeekDataList = ArrayList<WeatherOneWeekData>()
    private val allLocationData = ArrayList<WeatherOneWeekLocations>()
    private var area: String = ""
    private var city: String = ""

    init {
        if (Tool.isHasGPSPermission()) {
            searchLocationLiveData.value = true
        } else {
            requestGPSPermissionLiveData.value = true
        }
    }

    fun onStartToFlow() {
        showLocationName.value = city.ifEmpty { Tool.getLocationList()[0].first }
        locationListLiveData.value = Tool.getLocationList()
        searchWeatherByLocation(city.ifEmpty { Tool.getLocationList()[0].first })
        showCurrentTime()
        searchWeatherByLocationForOneWeek(getLocationCode())
    }

    private fun getLocationCode(): String {
        if (city.isNotEmpty()) {
            var locationCode = ""
            for (pair in Tool.getLocationList()) {
                if (pair.first == city) {
                    locationCode = pair.second
                    break
                }
            }
            return locationCode
        }
        return Tool.getLocationList()[0].second
    }

    private fun searchWeatherByLocationForOneWeek(locationKey: String) {
        mCompositeSubscription.add(ApiWrapper.getForecastByLocationForOneWeek(locationKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                showErrorMsg(R.string.internet_error.toString())
            }.subscribe({
                it.record?.locationList?.let { locationList ->
                    if (area.isNotEmpty()) {
                        targetSecondLocationName = area
                        showSecondLocationName.value = targetSecondLocationName
                    } else if (locationList.isNotEmpty() && locationList[0].locationDataList != null) {
                        if (locationList[0].locationDataList!!.isNotEmpty()) {
                            locationList[0].locationDataList!![0].locationName?.let { locationName ->
                                targetSecondLocationName = locationName
                                showSecondLocationName.value = locationName
                            }
                        }
                    }
                    Log.i("Michael", "targetSecondLocationName : $targetSecondLocationName")

                    if (targetSecondLocationName.isEmpty()) {
                        showErrorMsg("未知錯誤 , 請稍後再試")
                        return@subscribe
                    }

                    allLocationData.clear()
                    allLocationData.addAll(locationList)
                    handleLocationList(locationList)
                    showSecondLocationList(locationList)
                }

            }, {
                showErrorMsg(R.string.internet_error.toString())
            })
        )
    }

    private fun showSecondLocationList(locationList: MutableList<WeatherOneWeekLocations>) {
        val list = mutableListOf<String>()
        for (data in locationList) {
            data.locationDataList?.let {
                for (location in it) {
                    location.locationName?.let { locationName ->
                        list.add(locationName)
                    }
                }
            }
        }
        if (list.isEmpty()) {
            return
        }
        showSecondLocationName.value = area.ifEmpty { list[0] }
        showLocationList.value = list
    }

    private fun handleLocationList(locationList: MutableList<WeatherOneWeekLocations>) {
        oneWeekDataList.clear()
        for (data in locationList) {
            if (data.locationDataList.isNullOrEmpty()) {
                break
            }
            handleLocationData(data.locationDataList)
        }
        Log.i("Michael", "迴圈跑完了")
        showOneWeekForecastLiveData.value = oneWeekDataList

    }

    private fun handleLocationData(locationDataList: ArrayList<WeatherOneWeekLocationData>) {
        Log.i("Michael", "handleLocationData")
        for (data in locationDataList) {
            if (data.locationName != null && data.locationName == targetSecondLocationName) {
                data.elementList?.let {
                    handleWeatherElement(it)
                }
            }
        }
    }

    private fun handleWeatherElement(elementList: ArrayList<WeatherOneWeekElement>) {
        Log.i("Michael", "handleWeatherElement")
        for (data in elementList) {
            if (data.elementName != null && data.elementName == "T" && !data.timeList.isNullOrEmpty()) {
                handleTimeList(data.timeList)
            }
        }
    }

    private fun handleTimeList(timeList: ArrayList<WeatherOneWeekTime>) {
        Log.i("Michael", "handleTimeList")
        for (data in timeList) {
            if (data.startTime == null || data.endTime == null || data.elementValueList.isNullOrEmpty()) {
                continue
            }

            oneWeekDataList.add(
                WeatherOneWeekData(
                    data.startTime.formatTime(),
                    data.endTime.formatTime(),
                    data.elementValueList[0].value!! + "°C",
                    "${data.startTime.formatTime()}\n|\n${data.endTime.formatTime()}"
                )
            )
        }
    }

    private fun searchWeatherByLocation(location: String) {
        minT = ""
        maxT = ""
        mCompositeSubscription.add(ApiWrapper.getLocationWeather36HoursForecast(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                showErrorMsg(R.string.internet_error.toString())
            }
            .subscribe({

                it.records?.locationList?.let { locationList ->
                    for (data in locationList) {
                        checkWeatherElement(data.weatherElement)
                    }
                }
            }, {
                showErrorMsg(R.string.internet_error.toString())
            })
        )
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
        Log.i("Michael", "type : $type")
        var isShowWx = false
        var isShowPoP = false
        var isShowTemp = false
        var isShowCI = false
        val isFoundWx = type == "Wx"
        val isFoundPop = type == "PoP"
        val isFountTemp = type == "MinT" || type == "MaxT"
        val isFoundCI = type == "CI"

        for (time in timeList) {
            val startTime = formatTime(time.startTime)
            val endTime = formatTime(time.endTime)
            if (time.parameter?.parameterName == null) {
                Log.i("Michael", "錯誤 無資料")
                continue
            }

            if (System.currentTimeMillis() in startTime..endTime) {
                when (type) {
                    "Wx" -> {
                        isShowWx = true
                        checkStatus(time.parameter.parameterValue!!)
                    }

                    "PoP" -> {
                        isShowPoP = true
                        showPopPercentLiveData.value = time.parameter.parameterName + "%"
                    }

                    "MinT" -> {
                        isShowTemp = true
                        minT = time.parameter.parameterName
                    }

                    "MaxT" -> {
                        isShowTemp = true
                        maxT = time.parameter.parameterName
                    }

                    "CI" -> {
                        isShowCI = true
                        showCiInformationLivedata.value = time.parameter.parameterName
                    }
                }
            }
        }
        if (minT.isNotEmpty() && maxT.isNotEmpty()) {
            showTempLiveData.value = "$minT~$maxT°C"
        }
        if (!isShowWx && isFoundWx) {
            checkStatus(timeList[0].parameter?.parameterValue!!)
        }
        if (!isShowPoP && isFoundPop) {
            showPopPercentLiveData.value = timeList[0].parameter?.parameterName + "%"
        }
        if (!isShowTemp && isFountTemp) {
            if (type == "MinT") {
                minT = timeList[0].parameter?.parameterName!!
            }
            if (type == "MaxT") {
                maxT = timeList[0].parameter?.parameterName!!
            }
            if (minT.isNotEmpty() && maxT.isNotEmpty()) {
                showTempLiveData.value = "$minT~$maxT°C"
            }
        }
        if (!isShowCI && isFoundCI) {
            showCiInformationLivedata.value = timeList[0].parameter?.parameterName
        }
    }

    private fun checkStatus(parameterValue: String) {

        when (parameterValue) {
            "4" -> {
                showCloudyLiveData.value = true
            }

            in listOf("2", "3") -> {
                showCloudyAndSunLiveData.value = true
            }

            "1" -> {
                showSunLiveData.value = true
            }

            in listOf("5", "6") -> {
                showCloudyAndCloudyDarkLiveData.value = true
            }

            "7" -> {
                showCloudyDarkLiveData.value = true
            }

            "8" -> {
                showCloudyAndRainLiveData.value = true
            }

            in listOf("9", "10", "11", "12", "13", "14") -> {
                showCloudyDarkAndRainLiveData.value = true
            }

            else -> {
                showCloudyDarkAndRainWithThunderLiveData.value = true
            }
        }
    }

    fun onSearchBarClickListener() {
        isOpenLocationList = !isOpenLocationList
        startAnimationLocationListLiveData.value =
            if (isOpenLocationList) Pair(0f, 1f) else Pair(1f, 0f)
    }

    fun onCitySelectedListener(pair: Pair<String, String>) {
        city = ""
        area = ""
        showLocationName.value = pair.first
        isOpenLocationList = false
        startAnimationLocationListLiveData.value = Pair(1f, 0f)
        rainingDisposable?.dispose()
        thunderDisposable?.dispose()
        searchWeatherByLocation(pair.first)
        searchWeatherByLocationForOneWeek(pair.second)

    }

    fun onLocationSelectedListener(locationName: String) {
        showSecondLocationName.value = locationName
        isOpenSecondLocationList = false
        startAnimationSecondLocationListLiveData.value = Pair(1f, 0f)
        targetSecondLocationName = locationName
        handleLocationList(allLocationData)
    }

    fun onDestroy() {
        clearCompositeDisposable()
    }

    fun onStartCloudyAndSunAnimation() {
        viewIndex = 0
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

    fun onStartSunAnimation() {
        showSunSpinAnimationLiveData.value = R.id.sun
    }

    fun startToShowCloudyAndSunAnimation() {
        if (viewIndex < cloudyAndSunList.size) {
            if (cloudyAndSunList[viewIndex] == R.id.sun) {
                showSunSpinAnimationLiveData.value = cloudyAndSunList[viewIndex]
            } else {
                showCloudyAndSunAnimationLiveData.value = cloudyAndSunList[viewIndex]
            }
            viewIndex++
        } else {
            viewIndex = 0
        }
    }

    fun onStartCloudyAnimation() {
        viewIndex = 0
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
        if (viewIndex < cloudyList.size) {
            showCloudyAnimationLiveData.value = cloudyList[viewIndex]
            viewIndex++
        } else {
            viewIndex = 0
        }
    }

    fun onReadyToShowRaining(targetLeftX: Int, targetRightX: Int, targetY: Int) {

        rainingDisposable = Observable.interval(50, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val x = Random().nextInt(targetRightX) + targetLeftX
                showRainLiveData.value = Pair(x.toFloat(), targetY.toFloat())
            }, {
                showErrorMsg("動畫發生錯誤,請稍後再試")
            })
        rainingDisposable?.let {
            mCompositeSubscription.add(it)
        }

    }

    fun onReadyToShowThunder(targetLeftX: Int, targetRightX: Int, targetY: Int) {
        thunderDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val x = Random().nextInt(targetRightX) + targetLeftX
                showThunderLiveData.value = Pair(x.toFloat(), targetY.toFloat())
            }, {
                showErrorMsg("動畫發生錯誤,請稍後再試")
            })
        thunderDisposable?.let {
            mCompositeSubscription.add(it)
        }

    }

    fun onLocationBarClickListener() {
        isOpenSecondLocationList = !isOpenSecondLocationList
        startAnimationSecondLocationListLiveData.value =
            if (isOpenSecondLocationList) Pair(0f, 1f) else Pair(1f, 0f)
    }

    fun onCatchStateAndArea(city: String?, state: String?) {
        if (city == null || state == null) {
            onStartToFlow()
            return
        }
        this.area = city
        this.city = state.replace("台", "臺")
        Log.i("Michael", "city : ${this.city} area : $area")
        onStartToFlow()

    }


}