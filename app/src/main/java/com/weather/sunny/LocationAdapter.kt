package com.weather.sunny

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weather.sunny.databinding.ItemLocationLayoutBinding

class LocationAdapter(private val dataList: MutableList<Pair<String,String>> , private val onLocationSelectedListener: OnLocationSelectedListener) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: ItemLocationLayoutBinding,
        private val onLocationSelectedListener: OnLocationSelectedListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun showCity(pair: Pair<String, String>) {
            binding.data = pair.first
            binding.executePendingBindings()
            binding.rootView.setOnClickListener {
                onLocationSelectedListener.onCitySelected(pair)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,onLocationSelectedListener)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showCity(dataList[position])
    }

    fun interface OnLocationSelectedListener{
        fun onCitySelected(pair:Pair<String,String>)
    }

}