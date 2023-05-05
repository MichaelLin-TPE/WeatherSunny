package com.weather.sunny

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.weather.sunny.base.BaseViewModel
import com.weather.sunny.http.ApiWrapper
import com.weather.sunny.tool.Tool
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel(application) {

    val locationListLiveData = MutableLiveData<MutableList<String>>()
    val showLocationName = MutableLiveData<String>()
    val startAnimationLocationListLiveData = MutableLiveData<Pair<Float, Float>>()
    private var isOpenLocationList = false

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

            },{
                showErrorMsg(R.string.internet_error.toString())
            }))
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


}