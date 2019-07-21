package com.dekisolutions.holidaychecker.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dekisolutions.holidaychecker.common.BaseAndroidViewModel
import com.dekisolutions.holidaychecker.common.Logger
import com.dekisolutions.holidaychecker.common.SingleLiveEvent
import com.dekisolutions.holidaychecker.common.Utils
import com.dekisolutions.holidaychecker.data.select.HolidayResult
import com.dekisolutions.holidaychecker.network.response.Country
import com.dekisolutions.holidaychecker.network.response.Holiday
import com.dekisolutions.holidaychecker.usecase.GetCountriesUseCase
import com.dekisolutions.holidaychecker.usecase.GetHolidaysUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

class HolidayCheckerViewModel(
    application: Application,
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
) : BaseAndroidViewModel(application) {
    private val tag = "HolidayCheckerViewModel"

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd")
    private val countriesLiveData: MutableLiveData<List<Country>> = MutableLiveData()
    private val selectedCountriesLiveData = MutableLiveData<Pair<Country?, Country?>>()
    private val allHolidayLiveData: LiveData<Pair<List<Holiday>, List<Holiday>>> =
        Transformations.switchMap(selectedCountriesLiveData, Function { it ->
            Logger.d("switch trigger $it")
            val result = MutableLiveData<Pair<List<Holiday>, List<Holiday>>>()
            if (it.first != null && it.first!!.code.isNotEmpty() && it.second != null && it.second!!.code.isNotEmpty()) {
                addDisposable(getHolidaysUseCase.execute(Pair(it.first!!.code, it.second!!.code)).subscribe({
                    Logger.d("result ready")
                    result.postValue(it)
                }, {
                    Log.d(tag, it.message)
                    if (it is UnknownHostException) {
                        noInternetLiveData.postValue(it.message)
                    }
                }))
            }
            return@Function result
        })
    val moveToNextEnabledLiveDate: LiveData<Boolean> = Transformations.switchMap(selectedCountriesLiveData, Function {
        val result = MutableLiveData<Boolean>()
        result.value =
            (it.first != null && it.first!!.code.isNotEmpty()) && (it.second != null && it.second!!.code.isNotEmpty())
        return@Function result
    })
    private val commonHolidayLiveData: LiveData<List<HolidayResult>> = Transformations.switchMap(allHolidayLiveData, Function { pair ->
        val result = MutableLiveData<List<HolidayResult>>()
        val all = pair.first + pair.second
        val commonList = all.groupBy { it.date }
            .filter { it.value.size > 1 }
            .map {
                val myHoliday = it.value[0]
                val partnerHoliday = it.value[1]
                return@map HolidayResult(myHoliday.name, partnerHoliday.name, myHoliday.date, "")
            }
        var lastIndexValue: HolidayResult? = null
        val commonGroupList = mutableListOf<HolidayResult>()
        commonList.forEachIndexed { _, holidayResult ->
            if (lastIndexValue == null) {
                commonGroupList.add(holidayResult)
                lastIndexValue = holidayResult
            } else {
                val addDays: Date = if (lastIndexValue!!.endDate == "") {
                    Utils.addDays(format.parse(lastIndexValue!!.startDate), 1)
                } else {
                    Utils.addDays(format.parse(lastIndexValue!!.endDate), 1)
                }

                val currentDate = format.parse(holidayResult.startDate)
                if (addDays == currentDate) {
                    lastIndexValue!!.endDate = holidayResult.startDate
                } else {
                    commonGroupList.add(holidayResult)
                    lastIndexValue = holidayResult
                }
            }
        }
        result.postValue(commonGroupList)
        return@Function result
    })

    private val myHolidayLiveDate: LiveData<List<HolidayResult>> = Transformations.switchMap(allHolidayLiveData, Function { pair ->
        val result = MutableLiveData<List<HolidayResult>>()
        val partnerDateMap = pair.second.groupBy {
            it.date
        }
        val filteredResult = pair.first.filter {
             partnerDateMap[it.date] == null
        }. map { return@map HolidayResult(it.name, "", it.date, "") }
        result.postValue(filteredResult)
        return@Function result
    })

    private val partnerHolidayLiveDate: LiveData<List<HolidayResult>> = Transformations.switchMap(allHolidayLiveData, Function { pair ->
        val result = MutableLiveData<List<HolidayResult>>()
        val myDateMap = pair.first.groupBy {
            it.date
        }
        val filteredResult = pair.second.filter {
            myDateMap[it.date] == null
        }.map {
            return@map HolidayResult("", it.name, it.date, "")
        }
        result.postValue(filteredResult)
        return@Function result
    })
    private val selectedCommonFilter = MutableLiveData<Int>()
    val holidayResultLiveData: LiveData<List<HolidayResult>> = Transformations.switchMap(selectedCommonFilter, Function {
        when(it) {
            0 -> return@Function commonHolidayLiveData
            1 -> return@Function myHolidayLiveDate
            2 -> return@Function partnerHolidayLiveDate
            else -> return@Function commonHolidayLiveData
        }
    })
    val moveToNextLiveDate = SingleLiveEvent<Boolean>()
    val noInternetLiveData = MutableLiveData<String>()

    init {
        fetchCountries()
    }

    fun getCountries(): LiveData<List<Country>> {
        return countriesLiveData
    }

    fun fetchCountries() {
        addDisposable(
            getCountriesUseCase.execute().observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    countriesLiveData.postValue(it)
                },
                {
                    if(it is UnknownHostException) {
                        noInternetLiveData.postValue(it.message)
                    }
                    Log.d(tag, it.message)
                })
        )
    }

    fun setYourCountry(country: Country?) {
        Logger.d("your country: " + country.toString())
        selectedCountriesLiveData.value = Pair(country, selectedCountriesLiveData.value?.second)
    }

    fun setPartnerCountry(country: Country?) {
        Logger.d("partner country: " + country.toString())
        selectedCountriesLiveData.value = Pair(selectedCountriesLiveData.value?.first, country)
    }

    fun setOptionFilter(option: Int) {
        Logger.d("filter: $option")
        selectedCommonFilter.value = option
    }
}