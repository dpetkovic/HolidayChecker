package com.dekisolutions.holidaychecker.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dekisolutions.holidaychecker.R
import com.dekisolutions.holidaychecker.common.Logger
import com.dekisolutions.holidaychecker.ui.HolidayCheckerViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ResultFragment: Fragment() {

    private val viewModel: HolidayCheckerViewModel by sharedViewModel()
    private val adapter = ResultListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        initRecyclerView()
        observerData()
    }

    private fun initSpinner() {
        spinnerOption.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.setOptionFilter(position)
            }

        }
    }

    private fun observerData() {
        viewModel.holidayResultLiveData.observe(this, Observer {
            Logger.d(it.toString())
            adapter.submitList(it)
        })
    }

    private fun initRecyclerView() {

        val manager = LinearLayoutManager(context)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(recyclerView.context, manager.orientation)
        recyclerView.addItemDecoration(divider)
    }

}