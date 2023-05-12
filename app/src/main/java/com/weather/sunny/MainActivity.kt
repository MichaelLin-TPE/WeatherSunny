package com.weather.sunny

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.weather.sunny.base.BaseActivity
import com.weather.sunny.databinding.ActivityMainBinding
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
            showCloudy()
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
        viewModel.showCloudyAndSunLiveData.observe(this){
            showCloudyAndSun()
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
                animator.repeatMode = ObjectAnimator.REVERSE
                animator.repeatCount = ObjectAnimator.INFINITE
                animator.duration = (Random().nextInt(500 + 1) + 500).toLong()
                animator.start()
            }
        }

        viewModel.showSunLiveData.observe(this){
            showSun()
        }

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

    private fun showLocation(dataList: MutableList<String>) {
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