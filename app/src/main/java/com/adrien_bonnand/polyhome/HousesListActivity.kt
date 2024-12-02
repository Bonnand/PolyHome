package com.adrien_bonnand.polyhome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HousesListActivity : AppCompatActivity() {
    private val houses: ArrayList<House> = ArrayList()
    private lateinit var housesAdapter: HouseAdapter


    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_houses_list)
        token=intent.getStringExtra("token")
        initHousesListView()
        loadHouses()
    }

    override fun onResume() {
        super.onResume()
        loadHouses()
    }

    public fun loadHouses() {
        Api().get<ArrayList<House>>("https://polyhome.lesmoulinsdudev.com/api/houses", ::loadHousesSuccess,token)
    }

    public fun loadHousesSuccess(responseCode: Int, loadedHouses: ArrayList<House>?) {
        if (responseCode == 200 && loadedHouses != null) {
            houses.clear()
            houses.addAll(loadedHouses)
            runOnUiThread { housesAdapter.notifyDataSetChanged() }
        }
    }

    private fun initHousesListView() {
        val listView = findViewById<ListView>(R.id.lstHouses)
        housesAdapter = HouseAdapter(this, houses)
        listView.adapter = housesAdapter
    }

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
            val rowView = inflater.inflate(R.layout.show_houses, parent, false)
            val house = getItem(position)
            val houseIdText = rowView.findViewById<TextView>(R.id.textHouseId)
            val ownerText = rowView.findViewById<TextView>(R.id.textOwner)


            ownerText.text = if(house.owner) "Propriétaire" else "Membre"
            houseIdText.text = house.houseId.toString()

            return rowView
        }
    }

}