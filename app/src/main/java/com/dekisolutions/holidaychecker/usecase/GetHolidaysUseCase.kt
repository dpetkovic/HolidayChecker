package com.dekisolutions.holidaychecker.usecase

import com.dekisolutions.holidaychecker.data.HolidayRepository
import com.dekisolutions.holidaychecker.network.response.Holiday
import com.dekisolutions.holidaychecker.network.response.HolidaysResponse
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class GetHolidaysUseCase(private val holidayRepository: HolidayRepository) {

    fun execute(pair: Pair<String, String>): Single<Pair<List<Holiday>, List<Holiday>>> {
        return holidayRepository.getHolidays(pair.first)
            .zipWith(holidayRepository.getHolidays(pair.second),
                BiFunction { t1: HolidaysResponse, t2: HolidaysResponse -> Pair(t1, t2) })
            .subscribeOn(Schedulers.io()).map { t -> Pair(t.first.holidays, t.second.holidays) }
    }
}