package com.weather.sunny

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.sunny.bean.WeatherOneWeekData
import com.weather.sunny.databinding.ItemOneWeatherLayoutBinding
import com.weather.sunny.tool.Tool

class OneWeekAdapter(private val dataList: MutableList<WeatherOneWeekData>) :
    RecyclerView.Adapter<OneWeekAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemOneWeatherLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherOneWeekData: WeatherOneWeekData) {
            binding.data = weatherOneWeekData
            binding.executePendingBindings()

            binding.rootView.post {
                val originalHeight = binding.viewTemp.height
                val rootHeight = binding.rootView.height
                val dateTextHeight = binding.tvTime.height
                val tempTextHeight = binding.tvTempValue.height
                val leftHeight = rootHeight - dateTextHeight - tempTextHeight
                val percent = (weatherOneWeekData.temp.replace("°C", "").toInt() / 50f)
                val height = (leftHeight * percent).toInt()
                val layoutParams = binding.viewTemp.layoutParams
                val valueAnimator = ValueAnimator.ofInt(originalHeight,height)
                valueAnimator.addUpdateListener {
                    layoutParams.height = it.animatedValue as Int
                    binding.viewTemp.layoutParams = layoutParams
                }
                valueAnimator.duration = 200
                valueAnimator.start()

            }
            when (weatherOneWeekData.temp.replace("°C", "").toInt()) {
                in 1..15 -> {
                    binding.viewTemp.setBackgroundColor(Tool.getColor(R.color.cold))
                }
                in 16..25 -> {
                    binding.viewTemp.setBackgroundColor(Tool.getColor(R.color.confortable))
                }
                in 26..30 -> {
                    binding.viewTemp.setBackgroundColor(Tool.getColor(R.color.warm))
                }
                else -> {
                    binding.viewTemp.setBackgroundColor(Tool.getColor(R.color.hot))
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOneWeatherLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}