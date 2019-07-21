package com.dekisolutions.holidaychecker.network

import com.dekisolutions.holidaychecker.network.response.CountriesResponse
import com.dekisolutions.holidaychecker.network.response.HolidaysResponse
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



interface HolidayApi {

		@GET("v1/countries")
		fun getCountries(@Query("key") apiKey: String): Single<CountriesResponse>

		@GET("v1/holidays")
		fun getHolidays(@Query("key") apiKey: String, @Query("country") country: String,
				@Query("year") year: String = "2018"): Single<HolidaysResponse>
}

private const val baseUrl = "https://holidayapi.com"

fun createHolidayApi(): HolidayApi {
	val interceptor = HttpLoggingInterceptor()
	interceptor.level = HttpLoggingInterceptor.Level.BODY
	val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

	val retrofit = Retrofit.Builder()
		.baseUrl(baseUrl)
		.client(client)
		.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
		.addConverterFactory(GsonConverterFactory.create())
		.build()
	return retrofit.create(HolidayApi::class.java)
}