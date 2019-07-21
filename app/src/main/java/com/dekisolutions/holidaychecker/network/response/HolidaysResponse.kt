package com.dekisolutions.holidaychecker.network.response

import com.google.gson.annotations.SerializedName

data class HolidaysResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("holidays")
    val holidays: List<Holiday>
)

data class Holiday(
    @SerializedName("name")
    val name: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("observed")
    val observed: String,
    @SerializedName("public")
    val isPublic: Boolean,
    @SerializedName("country")
    val country: String
)

