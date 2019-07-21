package com.dekisolutions.holidaychecker.ui.select

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.dekisolutions.holidaychecker.R
import com.dekisolutions.holidaychecker.network.response.Country
import com.squareup.picasso.Picasso

class CountrySpinnerAdapter(context: Context, list: List<Country>, resId: Int = 0) : ArrayAdapter<Country>(context, resId, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_country, parent, false)
        }
        val item = getItem(position)
        item ?: return view!!

        val textViewName = view?.findViewById<TextView>(R.id.textViewName)
        textViewName?.text = item.name
        val imageView = view?.findViewById<ImageView>(R.id.imageViewFlag)
        if (item.flag != null && item.flag.isNotEmpty()) {
            Picasso.get().load(item.flag).into(imageView)
        } else {
            imageView?.setImageBitmap(null)
        }
        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}