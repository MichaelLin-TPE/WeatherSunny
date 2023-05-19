package com.weather.sunny

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.sunny.bean.WeatherOneWeekData
import com.weather.sunny.databinding.ItemOneWeatherLayoutBinding

class OneWeekAdapter(private val dataList: MutableList<WeatherOneWeekData>) :
    RecyclerView.Adapter<OneWeekAdapter.ViewHolder>() {


    class ViewHolder(private val binding: ItemOneWeatherLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherOneWeekData: WeatherOneWeekData) {
            binding.data = weatherOneWeekData
            binding.executePendingBindings()

            binding.rootView.post {
                Log.i("Michael","item height : ${binding.rootView.height}")
                val rootHeight = binding.rootView.height
                val dateTextHeight = binding.tvTime.height
                val tempTextHeight = binding.tvTempValue.height
                val leftHeight = rootHeight - dateTextHeight - tempTextHeight
                val percent = (weatherOneWeekData.temp.replace("°C","").toInt() / 100f)
                val height = (leftHeight * percent).toInt()
                val layoutParams = binding.viewTemp.layoutParams
                layoutParams.height = height
                binding.viewTemp.layoutParams = layoutParams

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