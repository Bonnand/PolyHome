package com.adrien_bonnand.polyhome.User

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.adrien_bonnand.polyhome.House.HouseUserData
import com.adrien_bonnand.polyhome.R

class UserAdapter(
    private val context: Context,
    private val dataSource: ArrayList<HouseUserData>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): HouseUserData {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val user = getItem(position)

        val rowView = inflater.inflate(R.layout.show_users, parent, false)
        val userLoginText = rowView.findViewById<TextView>(R.id.textUserLogin)
        val userRoleText = rowView.findViewById<TextView>(R.id.textUserRole)

        userLoginText.text = user.userLogin
        if (user.owner == 1) {
            userRoleText.text = "Propriétaire"
        } else {
            userRoleText.text = "Membre"
        }

        return rowView
    }
}