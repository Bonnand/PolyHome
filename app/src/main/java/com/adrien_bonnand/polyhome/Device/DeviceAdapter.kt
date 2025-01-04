package com.adrien_bonnand.polyhome.Device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
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


        if (device.id.startsWith("Light")) {

            deviceIdText.text = device.id.replace("Light", "Lumière")

            buttonOpen.text = "I"
            buttonClose.text = "O"
            buttonStop.visibility = View.GONE

            deviceStateText.text = if (device.power == 1) "Allumée" else "Éteinte"

            buttonOpen.setOnClickListener {
                (context as? DevicesListActivity)?.sendDeviceCommand(device.id, "TURN ON")
            }

            buttonClose.setOnClickListener {
                (context as? DevicesListActivity)?.sendDeviceCommand(device.id, "TURN OFF")
            }
        } else {
            if (device.id.startsWith("Shutter")) {
                deviceIdText.text = device.id.replace("Shutter", "Volet")
            } else {
                deviceIdText.text = device.id.replace("GarageDoor", "Garage")

            }
            buttonOpen.text = "▲"
            buttonClose.text = "▼"
            buttonStop.visibility = View.VISIBLE
            buttonStop.text = "■"

            if (device.openingMode == 0) {
                deviceStateText.text = "Ouvert"
            } else if (device.openingMode == 1) {
                deviceStateText.text = "Fermé"
            } else if (device.openingMode == 2) {
                deviceStateText.text = "Entrouvert"
            } else {
            deviceStateText.text = "État inconnu"
            }

            buttonOpen.setOnClickListener {
                (context as? DevicesListActivity)?.sendDeviceCommand(device.id, "OPEN")
            }

            buttonClose.setOnClickListener {
                (context as? DevicesListActivity)?.sendDeviceCommand(device.id, "CLOSE")
            }

            buttonStop.setOnClickListener {
                (context as? DevicesListActivity)?.sendDeviceCommand(device.id, "STOP")
            }
        }
        return rowView
    }

}