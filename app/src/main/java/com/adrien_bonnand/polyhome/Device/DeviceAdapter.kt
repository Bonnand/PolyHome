package com.adrien_bonnand.polyhome.Device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.CommandData
import com.adrien_bonnand.polyhome.R

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

        val buttonOpen = rowView.findViewById<Button>(R.id.buttonOpen)
        val buttonClose = rowView.findViewById<Button>(R.id.buttonClose)
        val buttonStop = rowView.findViewById<Button>(R.id.buttonStop)

        deviceIdText.text = device.id

        if (device.type == "light") {
            deviceStateText.text = if (device.power == 1) "Allumé" else "Éteint"
        } else {
            deviceStateText.text = if (device.opening == 1) "Ouvert" else "Fermé"
        }

        buttonOpen.setOnClickListener {
            sendDeviceCommand(device.id, "OPEN")
        }

        buttonClose.setOnClickListener {
            sendDeviceCommand(device.id, "CLOSE")
        }

        buttonStop.setOnClickListener {
            sendDeviceCommand(device.id, "STOP")
        }

        return rowView
    }

    public fun testSucess(responseCode: Int)
    {

    }

    public fun commandDeviceSuccess(responseCode: Int) {

    }

    private fun test()
    {
        val commandData = CommandData("");
        Api().post<CommandData>("https://polyhome.lesmoulinsdudev.com/",commandData,::testSucess)
    }

    private fun sendDeviceCommand(deviceId: String, command: String) {

        val houseId = 81 //à supprimer ensuite
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjgxLCJpYXQiOjE3MzMxMzU4OTZ9.gO66kzH_4wPfoHlP-102UBFC8KSqcrrPM787YA6wR4Y"
        val commandData = CommandData(command=command)
        Api().post<CommandData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command",commandData,::commandDeviceSuccess,token)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            (context as? DevicesListActivity)?.loadDevices()
        }, 10000)
    }

}