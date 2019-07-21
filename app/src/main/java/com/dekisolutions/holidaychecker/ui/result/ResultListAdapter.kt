package com.dekisolutions.holidaychecker.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dekisolutions.holidaychecker.R
import com.dekisolutions.holidaychecker.data.select.HolidayResult

class ResultListAdapter :
    ListAdapter<HolidayResult, ResultViewHolder>(object : DiffUtil.ItemCallback<HolidayResult>() {
        override fun areItemsTheSame(oldItem: HolidayResult, newItem: HolidayResult): Boolean {
            return oldItem.myHolidayName == newItem.myHolidayName
        }

        override fun areContentsTheSame(oldItem: HolidayResult, newItem: HolidayResult): Boolean {
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_result, parent, false))
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.setup(getItem(position))
    }
}

class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val textViewDate: TextView = view.findViewById(R.id.textViewDate)
    private val textViewPartnerHoliday: TextView = view.findViewById(R.id.textViewPartnerHoliday)
    private val textViewMyHoliday: TextView = view.findViewById(R.id.textViewMyHoliday)
    private val textViewDateEnd: TextView = view.findViewById(R.id.textViewDateEnd)


    fun setup(holidayResult: HolidayResult) {
        textViewMyHoliday.text = holidayResult.myHolidayName
        textViewPartnerHoliday.text = holidayResult.partnerHolidayName
        textViewDate.text = holidayResult.startDate
        if (holidayResult.endDate == "") {
            textViewDateEnd.text = holidayResult.startDate
        } else {
            textViewDateEnd.text = holidayResult.endDate
        }
    }
}