package com.weather.sunny

import android.R.attr.banner
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.weather.sunny.databinding.ActivityTestBinding
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator


class TestActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_test)
        val pic1 = "https://images.chinatimes.com/newsphoto/2021-08-17/1024/20210817003387.jpg"
        val pic2 = "https://i0.wp.com/tinyhumanonboard.com/wp-content/uploads/2022/06/tigerpic-e1654549927355.jpeg?fit=889%2C580&ssl=1"
        val pic3 = "https://s.yimg.com/ny/api/res/1.2/YfAD3ouD7DAggkZGksKd1Q--/YXBwaWQ9aGlnaGxhbmRlcjt3PTk2MDtoPTExMjA7Y2Y9d2VicA--/https://media.zenfs.com/en/nownews.hk/f529eaebc1cdf0ae076f5ecb5b2f80a0"
        val bannerPicList = mutableListOf<String>()
        bannerPicList.add(pic1)
        bannerPicList.add(pic2)
        bannerPicList.add(pic3)
        binding.banner.setAdapter(object : BannerImageAdapter<String>(bannerPicList) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: String,
                position: Int,
                size: Int
            ) {
                //图片加载自己实现
                Glide.with(holder.itemView)
                    .load(data)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageView)
            }
        }).addBannerLifecycleObserver(this) //添加生命周期观察者
            .setIndicator(CircleIndicator(this))

    }
}