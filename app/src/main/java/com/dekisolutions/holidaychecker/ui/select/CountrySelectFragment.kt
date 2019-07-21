package com.dekisolutions.holidaychecker.ui.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dekisolutions.holidaychecker.R
import com.dekisolutions.holidaychecker.common.Logger
import com.dekisolutions.holidaychecker.network.response.Country
import com.dekisolutions.holidaychecker.ui.HolidayCheckerViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_select_country.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CountrySelectFragment : Fragment() {

    private val viewModel: HolidayCheckerViewModel by sharedViewModel()
    private var countryAdapter: CountrySpinnerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_select_country, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCountrySpinners()
        initRetryButton()
        observeCounties()
        observeNext()
        observeInternet()
    }

    private fun initRetryButton() {
        buttonRetry.setOnClickListener {
            viewModel.fetchCountries()
            it.visibility = View.GONE
        }
    }

    private fun observeInternet() {
        viewModel.noInternetLiveData.observe(this,  Observer {
            buttonRetry.visibility = View.VISIBLE
        })
    }

    private fun observeNext() {
        viewModel.moveToNextEnabledLiveDate.observe(this,  Observer {
            if (it) {
                button.isEnabled = true
                button.text = "Next"
                button.setOnClickListener {
                    viewModel.moveToNextLiveDate.value = true
                }
            } else {
                button.isEnabled = false
                button.text = "Select First"
            }
        })
    }

    private fun observeCounties() {
        viewModel.getCountries().observe(this, Observer {
            countryAdapter?.clear()
            countryAdapter?.add(Country("", "Please select a country", "", ""))
            countryAdapter?.addAll(it)
            countryAdapter?.notifyDataSetChanged()
        })
    }

    private fun initCountrySpinners() {
        context ?: return
        countryAdapter =  CountrySpinnerAdapter(context!!, arrayListOf())
        spinnerCountry.adapter = countryAdapter
        spinnerCountry.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = countryAdapter?.getItem(position)
                viewModel.setYourCountry(item)
            }

        }

        spinnerPartnerCountry.adapter = countryAdapter
        spinnerPartnerCountry.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = countryAdapter?.getItem(position)
                viewModel.setPartnerCountry(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (countryAdapter?.isEmpty == true) {
                    viewModel.fetchCountries()
                }
            }

        }
    }
}