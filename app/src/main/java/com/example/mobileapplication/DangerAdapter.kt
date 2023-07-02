package com.example.mobileapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.withContext

class DangerAdapter : RecyclerView.Adapter<DangerAdapter.ViewHolder>() {

    private var dangerTimeInfo: List<String> = emptyList()
    private var dangerTempInfo: List<String> = emptyList()
    private var dangerRainInfo: List<String> = emptyList()
    private var dangerRainChanceInfo: List<String> = emptyList()
    private var imageResIdList: List<Int> = emptyList() // 새로운 이미지 리소스 ID 리스트

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.danger_time_info)
        val tempTextView: TextView = itemView.findViewById(R.id.danger_temp_info)
        val rainTextView: TextView = itemView.findViewById(R.id.danger_rain_info)
        val rainChanceTextView: TextView = itemView.findViewById(R.id.danger_rain_chance_info)
        val weatherImageView : ImageView = itemView.findViewById(R.id.danger_weather_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = dangerTimeInfo[position]
        val temp = dangerTempInfo[position]

        val rain = dangerRainInfo[position]
        val rainChance = dangerRainChanceInfo[position]
        val imageResId = imageResIdList[position] // 새로운 이미지 리소스 ID

        holder.timeTextView.text = time.slice(0..3) + "년 "+ time.slice(4..5)+ "월" + time.slice(6..7) + "일 " + time.slice(8..9) + "시"
        holder.tempTextView.text = "기온 : "+temp
        holder.rainTextView.text = "강수량 : "+rain
        holder.rainChanceTextView.text = "강수확률 : " +rainChance
        holder.weatherImageView.setImageResource(imageResId) // 이미지 설정
    }

    override fun getItemCount(): Int {
        return dangerTimeInfo.size
    }

    fun setData(
        timeInfo: List<String>,
        tempInfo: List<String>,
        rainInfo: List<String>,
        rainChanceInfo: List<String>,
        imageResIdList: List<Int>
    ) {
        if (timeInfo.isNotEmpty() && tempInfo.isNotEmpty() && rainInfo.isNotEmpty() && rainChanceInfo.isNotEmpty() && imageResIdList.isNotEmpty()) {
            dangerTimeInfo = timeInfo
            dangerTempInfo = tempInfo
            dangerRainInfo = rainInfo
            dangerRainChanceInfo = rainChanceInfo
            this.imageResIdList = imageResIdList
            notifyDataSetChanged()
        }
    }

}