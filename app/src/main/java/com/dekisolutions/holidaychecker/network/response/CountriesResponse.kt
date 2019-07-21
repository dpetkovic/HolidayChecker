package com.dekisolutions.holidaychecker.network.response

import com.google.gson.annotations.SerializedName

data class CountriesResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("countries")
    val countries: List<Country>
)

data class Country(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("flag")
    val flag: String?,
    @SerializedName("language")
    val language: String?
)