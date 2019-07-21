package com.dekisolutions.holidaychecker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dekisolutions.holidaychecker.R
import com.dekisolutions.holidaychecker.common.Logger
import com.dekisolutions.holidaychecker.ui.result.ResultFragment
import com.dekisolutions.holidaychecker.ui.select.CountrySelectFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayCheckerActivity : AppCompatActivity() {

    private val viewModel: HolidayCheckerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViewPager()

        observeData()
    }

    private fun observeData() {
        viewModel.moveToNextEnabledLiveDate.observe(this,  Observer {
            Logger.d("enabled: $it")
            viewPager.setPagingEnabled(it)
        })
        viewModel.moveToNextLiveDate.observe(this, Observer {
            viewPager.setCurrentItem(1, true)
        })
        viewModel.noInternetLiveData.observe(this, Observer { event: String ->
            Logger.d("No Internet")
            Snackbar.make(viewPager, "No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("Ok") {
            }.show()
        })
    }

    private fun initViewPager() {
        val countrySelectFragment = CountrySelectFragment()
        val resultFragment = ResultFragment()
        val fragments = arrayListOf(countrySelectFragment, resultFragment)
        val adapter = MainFragmentAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        if(viewPager.currentItem != 0) {
            viewPager.currentItem = 0
        } else {
            super.onBackPressed()

        }
    }
}
