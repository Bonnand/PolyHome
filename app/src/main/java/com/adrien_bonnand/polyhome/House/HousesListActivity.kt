package com.adrien_bonnand.polyhome.House

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.Login.LoginActivity
import com.adrien_bonnand.polyhome.R

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
            runOnUiThread {
                housesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initHousesListView() {
        val listView = findViewById<ListView>(R.id.lstHouses)
        val intentLeave = Intent(
            this,
            HouseInterfaceActivity::class.java
        )
        housesAdapter = HouseAdapter(this, houses)
        listView.adapter = housesAdapter
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {
                val houseSelected = houses[position]

                intentLeave.putExtra("token",token)
                intentLeave.putExtra("selectedHouse",houseSelected.houseId.toString())

                startActivity(intentLeave);

            }
        }
    }

    public fun switchUser(view: View) {
        val intentLeave = Intent(
            this,
            LoginActivity::class.java
        )
        startActivity(intentLeave);
    }

}