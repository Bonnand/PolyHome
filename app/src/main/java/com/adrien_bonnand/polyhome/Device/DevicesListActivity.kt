package com.adrien_bonnand.polyhome.Device

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
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.R

class DevicesListActivity : AppCompatActivity() {
    private val devices = ArrayList<Device>()
    private lateinit var devicesAdapter: DeviceAdapter
    //private var token: String? = null
    //private var houseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_devices_list)

        //token=intent.getStringExtra("token")
        //houseId=intent.getStringExtra("houseId")

        initDevicesListView()
        loadDevices()
        }

    override fun onResume() {
        super.onResume()
        loadDevices()
    }

    public fun loadDevices() {
        val houseId = 81
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjgxLCJpYXQiOjE3MzMxMzU4OTZ9.gO66kzH_4wPfoHlP-102UBFC8KSqcrrPM787YA6wR4Y"
        Api().get<Devices>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices", ::loadDevicesSuccess,token)
    }

    public fun loadDevicesSuccess(responseCode: Int, loadedDevices: Devices?) {
        if (responseCode == 200 && loadedDevices != null) {
            devices.clear()
            devices.addAll(loadedDevices.devices)
            runOnUiThread { devicesAdapter.notifyDataSetChanged() }
        }
    }

    private fun initDevicesListView() {
        val listView = findViewById<ListView>(R.id.lstDevices)
        devicesAdapter = DeviceAdapter(this, devices)
        listView.adapter = devicesAdapter
    }

    class DeviceAdapter(
        private val context: Context,
        private val dataSource: ArrayList<Device>

    ) : BaseAdapter() {

        private val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Device {
            return dataSource[position]
        }

        override fun getCount(): Int {
            return dataSource.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = inflater.inflate(R.layout.show_devices, parent, false)
            val device = getItem(position)
            val deviceIdText = rowView.findViewById<TextView>(R.id.textDeviceId)
            val deviceStateText = rowView.findViewById<TextView>(R.id.textDeviceState)

            deviceStateText.text = if(device.openingMode == 1) "Fermé" else "Ouvert"
            deviceIdText.text = device.deviceId

            return rowView
        }
    }
}
