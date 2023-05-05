package com.weather.sunny.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.weather.sunny.MainViewModel
import com.weather.sunny.application.MyApplication

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MyApplication.instance?.let {
                return MainViewModel(it) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}