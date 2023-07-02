package com.example.mobileapplication

data class SeoulData(
    val listTotalCount: Int,
    val areaName: String,
    val weatherStts: WeatherStts
)

data class WeatherStts(
    val temp: Double,
    val precipitation: String,
    val precptType: String,
    val pcpMsg: String,
    val uvIndexLvl: Int,
    val uvIndex: String,
    val uvMsg: String,
    val pm25Index: String,
    val pm25: Int,
    val pm10Index: String,
    val pm10: Int,
    val airIdx: String,
    val airIdxMvl: Int,
    val airIdxMain: String,
    val airMsg: String
)