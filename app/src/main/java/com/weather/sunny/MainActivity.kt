package com.weather.sunny

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.weather.sunny.base.BaseActivity
import com.weather.sunny.bean.Weather36HRecords
import com.weather.sunny.databinding.ActivityMainBinding
import com.weather.sunny.http.ApiService
import com.weather.sunny.http.ApiWrapper
import com.weather.sunny.tool.Tool
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = getViewModel(MainViewModel::class.java)
        initView()
        handleLiveData()
    }

    private fun handleLiveData() {
        viewModel.locationListLiveData.observe(this){dataList->
            showLocation(dataList)
        }
        viewModel.showLocationName.observe(this){
            binding.tvLocationName.text = it
        }

        viewModel.startAnimationLocationListLiveData.observe(this){
            val alphaAnimation = ObjectAnimator.ofFloat(binding.listBg,View.ALPHA,it.first,it.second)
            alphaAnimation.duration = 200
            alphaAnimation.start()
        }
    }

    private fun showLocation(dataList: MutableList<String>) {
        binding.locationList.apply {
            adapter = LocationAdapter(dataList){
                viewModel.onCitySelectedListener(it)
            }
        }
    }

    private fun initView() {
        binding.bg.setOnClickListener {
            viewModel.onSearchBarClickListener()
        }
        binding.listBg.alpha = 0f

    }
}