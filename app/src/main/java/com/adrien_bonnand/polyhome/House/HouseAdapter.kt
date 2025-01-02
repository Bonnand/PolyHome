package com.adrien_bonnand.polyhome.House

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.adrien_bonnand.polyhome.R

class HouseAdapter(
    private val context: Context,
    private val dataSource: ArrayList<House>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): House {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val house = getItem(position)

        val rowView = inflater.inflate(R.layout.show_houses, parent, false)
        val houseText = rowView.findViewById<TextView>(R.id.textUserLogin)
        val ownerText = rowView.findViewById<TextView>(R.id.textUserRole)

        houseText.text = house.houseId.toString()

        if (house.owner == true) {
            ownerText.text = "Propriétaire"
        } else {
            ownerText.text = "Membre"
        }


        return rowView
    }
}