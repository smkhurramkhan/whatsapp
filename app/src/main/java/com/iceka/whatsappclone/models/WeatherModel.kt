package com.iceka.whatsappclone.models

data class WeatherModel(
    var lowTemp: Int,
    var hightTemp: Int,
    var tempType: String,
    var tempDegree: Int,
    var image: Int,
    var day: String,
    var isHighOrLow: String
)
