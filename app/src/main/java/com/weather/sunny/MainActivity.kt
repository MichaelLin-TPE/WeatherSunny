package com.weather.sunny

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.weather.sunny.base.BaseActivity
import com.weather.sunny.databinding.ActivityMainBinding
import com.weather.sunny.tool.Tool.convertDp
import com.weather.sunny.tool.Tool.getLocationXY
import java.util.Random

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var weatherStatus : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = getViewModel(MainViewModel::class.java)
        initView()
        handleLiveData()

    }



    private fun showCloudyAndSun(){
        weatherStatus = View.inflate(this, R.layout.item_cloudy_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE
        val ivSun = weatherStatus.findViewById<ImageView>(R.id.sun)
        ivSun.visibility = View.VISIBLE
        viewModel.onStartCloudyAndSunAnimation()
    }
    private fun showCloudy() {
        weatherStatus = View.inflate(this, R.layout.item_cloudy_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE
        viewModel.onStartCloudyAnimation()


    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun handleLiveData() {
        viewModel.locationListLiveData.observe(this) { dataList ->
            showLocation(dataList)
        }
        viewModel.showLocationName.observe(this) {
            binding.tvLocationName.text = it
        }

        viewModel.startAnimationLocationListLiveData.observe(this) {
            val alphaAnimation =
                ObjectAnimator.ofFloat(binding.listBg, View.ALPHA, it.first, it.second)
            alphaAnimation.duration = 200
            alphaAnimation.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {
                    if (it.first == 0f){
                        binding.listBg.visibility = View.VISIBLE
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (it.first == 1f){
                        binding.listBg.visibility = View.INVISIBLE
                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            alphaAnimation.start()
        }
        viewModel.showCurrentTimeLiveData.observe(this) {
            binding.tvClock.text = it
        }
        viewModel.showCloudyLiveData.observe(this) {
            binding.weatherStatusView.removeAllViews()
            showCloudy()
        }
        viewModel.showCloudyAndSunLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyAndSun()
        }
        viewModel.showCloudyAndCloudyDarkLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyAndCloudyDark()
        }
        viewModel.showCloudyDarkLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyDark()
        }
        viewModel.showCloudyAndRainLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyAndRain()
        }
        viewModel.showCloudyAnimationLiveData.observe(this){
            val ivCloudy = weatherStatus.findViewById<ImageView>(it)
            ivCloudy.post {
                val animator = ObjectAnimator.ofFloat(ivCloudy, "x", ivCloudy.x, ivCloudy.x + 10f)
                animator.repeatMode = ObjectAnimator.REVERSE
                animator.repeatCount = ObjectAnimator.INFINITE
                animator.duration = (Random().nextInt(500 + 1) + 500).toLong()
                animator.start()
                viewModel.startToShowCloudyAnimation()
            }
        }

        viewModel.showCloudyAndSunAnimationLiveData.observe(this){
            val ivCloudy = weatherStatus.findViewById<ImageView>(it)
            ivCloudy.post {
                val animator = ObjectAnimator.ofFloat(ivCloudy, "x", ivCloudy.x, ivCloudy.x + 10f)
                animator.repeatMode = ObjectAnimator.REVERSE
                animator.repeatCount = ObjectAnimator.INFINITE
                animator.duration = (Random().nextInt(500 + 1) + 500).toLong()
                animator.start()
                viewModel.startToShowCloudyAndSunAnimation()
            }
        }

        viewModel.showSunSpinAnimationLiveData.observe(this){
            val ivSun = weatherStatus.findViewById<ImageView>(it)
            ivSun.post {
                val animator = ObjectAnimator.ofFloat(ivSun, "rotation", 0f, 360f)
                animator.repeatCount = ObjectAnimator.INFINITE
                animator.duration = (Random().nextInt(10000 + 1) + 10000).toLong()
                animator.start()
            }
        }

        viewModel.showCloudyDarkAndRainLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyDarkAndRain(false)
        }

        viewModel.showCloudyDarkAndRainWithThunderLiveData.observe(this){
            binding.weatherStatusView.removeAllViews()
            showCloudyDarkAndRain(true)
        }

        viewModel.showSunLiveData.observe(this){
            showSun()
        }

        viewModel.showThunderLiveData.observe(this){
            val rainView = View.inflate(this,R.layout.item_thunder_layout,null)
            rainView.visibility = View.INVISIBLE
            binding.rootView.addView(rainView)
            rainView.x = it.first
            rainView.y = it.second
            rainView.visibility = View.VISIBLE
            val layoutParamsMargin = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsMargin.setMargins(0,Random().nextInt(15.convertDp()),0,0)
            rainView.layoutParams = layoutParamsMargin
            val alphaAnimation =
                ObjectAnimator.ofFloat(rainView, View.ALPHA, 1f, 0f)
            alphaAnimation.duration = 700
            alphaAnimation.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    binding.rootView.removeView(rainView)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
            alphaAnimation.start()
        }

        viewModel.showRainLiveData.observe(this){
            val rainView = View.inflate(this,R.layout.item_rain_layout,null)
            rainView.visibility = View.INVISIBLE
            binding.rootView.addView(rainView)
            rainView.x = it.first
            rainView.y = it.second
            rainView.visibility = View.VISIBLE
            val layoutParamsMargin = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsMargin.setMargins(0,Random().nextInt(15.convertDp()),0,0)
            rainView.layoutParams = layoutParamsMargin

            val animator = ObjectAnimator.ofFloat(rainView, "y", rainView.y, rainView.y + Random().nextInt(100) + 300)
            animator.duration = (Random().nextInt(200 + 1) + 300).toLong()
            animator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    binding.rootView.removeView(rainView)
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }

            })
            animator.start()
        }

        viewModel.showPopPercentLiveData.observe(this){
            binding.tvPop.text = it
        }
        viewModel.showCiInformationLivedata.observe(this){
            val content = "體感 : $it"
            binding.tvCi.text = content
        }
        viewModel.showTempLiveData.observe(this){
            binding.tvTemp.text = it
        }

    }

    private fun showCloudyDarkAndRain(isShowThunder: Boolean) {
        weatherStatus = View.inflate(this, R.layout.item_cloudy_dark_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE

        val ivCloudyLeft = weatherStatus.findViewById<ImageView>(R.id.cloudy1)
        val ivCloudyRight = weatherStatus.findViewById<ImageView>(R.id.cloudy8)
        val rootView = weatherStatus.findViewById<ConstraintLayout>(R.id.weather_root_view)
        rootView.post {
            val targetLeftX = ivCloudyLeft.getLocationXY()[0]
            val targetY = ivCloudyLeft.getLocationXY()[1] + ivCloudyLeft.height
            val targetRightX = ivCloudyRight.getLocationXY()[0]
            viewModel.onReadyToShowRaining(targetLeftX,targetRightX,targetY)
            if (isShowThunder){
                viewModel.onReadyToShowThunder(targetLeftX,targetRightX,targetY)
            }
        }

        viewModel.onStartCloudyAnimation()
    }

    private fun showCloudyAndRain() {
        weatherStatus = View.inflate(this, R.layout.item_cloudy_and_rain_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE

        val ivCloudyLeft = weatherStatus.findViewById<ImageView>(R.id.cloudy1)
        val ivCloudyRight = weatherStatus.findViewById<ImageView>(R.id.cloudy8)
        val rootView = weatherStatus.findViewById<ConstraintLayout>(R.id.weather_root_view)
        rootView.post {
            val targetLeftX = ivCloudyLeft.getLocationXY()[0]
            val targetY = ivCloudyLeft.getLocationXY()[1] + ivCloudyLeft.height
            val targetRightX = ivCloudyRight.getLocationXY()[0]
            viewModel.onReadyToShowRaining(targetLeftX,targetRightX,targetY)
        }

        viewModel.onStartCloudyAnimation()
    }

    private fun showCloudyDark() {
        weatherStatus = View.inflate(this, R.layout.item_cloudy_dark_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE
        viewModel.onStartCloudyAnimation()
    }

    private fun showCloudyAndCloudyDark() {
        weatherStatus = View.inflate(this, R.layout.item_cloudy_and_cloudy_dark_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE
        viewModel.onStartCloudyAnimation()
    }

    private fun showSun() {
        weatherStatus = View.inflate(this, R.layout.item_sun_layout, null)
        weatherStatus.visibility = View.INVISIBLE
        binding.weatherStatusView.addView(weatherStatus)
        val layoutParams = weatherStatus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        weatherStatus.layoutParams = layoutParams
        weatherStatus.visibility = View.VISIBLE
        val ivSun = weatherStatus.findViewById<ImageView>(R.id.sun)
        ivSun.visibility = View.VISIBLE
        viewModel.onStartSunAnimation()
    }

    private fun showLocation(dataList: MutableList<Pair<String,String>>) {
        binding.locationList.apply {
            adapter = LocationAdapter(dataList) {
                viewModel.onCitySelectedListener(it)
            }
        }
    }

    private fun initView() {
        binding.searchView.setOnClickListener {
            viewModel.onSearchBarClickListener()
        }
        binding.listBg.alpha = 0f
        binding.listBg.visibility = View.INVISIBLE

    }
}