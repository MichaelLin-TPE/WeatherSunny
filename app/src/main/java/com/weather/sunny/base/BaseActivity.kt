package com.weather.sunny.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseActivity : AppCompatActivity() {

    private val viewModelFactory = ViewModelFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun <T : ViewModel> getViewModel(viewModelClass: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

}