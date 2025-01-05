package com.adrien_bonnand.polyhome.Device

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.CommandData
import com.adrien_bonnand.polyhome.R

class DevicesListActivity : AppCompatActivity() {
    val devices = ArrayList<Device>()
    private lateinit var devicesAdapter: DeviceAdapter
    private var token: String? = null
    private var houseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_devices_list)

        token=intent.getStringExtra("token")
        houseId=intent.getStringExtra("houseId")

        initDevicesListView()
        loadDevices()
        }

    override fun onResume() {
        super.onResume()
        loadDevices()
    }

    public fun goBack(view: View) {
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    public fun loadDevices() {
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

    public fun sendCommand(view: View){

        runOnUiThread {
            Toast.makeText(this, "Commande Envoyée", Toast.LENGTH_SHORT).show()
        }

        val spinnerCommand = findViewById<Spinner>(R.id.spinnerCommand)
        val spinnerHour = findViewById<Spinner>(R.id.spinnerHour)
        val spinnerMinute = findViewById<Spinner>(R.id.spinnerMinute)


        val command = spinnerCommand.selectedItem.toString()
        val hour = spinnerHour.selectedItem.toString()
        val minute = spinnerMinute.selectedItem.toString()

        val deviceFilter: String
        val deviceCommand: String

        if (command == "Fermer tous les volets") {
            deviceFilter = "Shutter"
            deviceCommand = "CLOSE"
        } else if (command == "Ouvrir tous les volets") {
            deviceFilter = "Shutter"
            deviceCommand = "OPEN"
        } else if (command == "Allumer toutes les lumières") {
            deviceFilter = "Light"
            deviceCommand = "TURN ON"
        } else {
            deviceFilter = "Light"
            deviceCommand = "TURN OFF"
        }

        if (hour == "HH" && minute == "MM"){
            for (device in devices) {
                if (device.id.contains(deviceFilter)) {
                    sendDeviceCommand(device.id, deviceCommand)
                }
            }
        } else {
            val currentTime = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
            calendar.set(Calendar.MINUTE, minute.toInt())
            calendar.set(Calendar.SECOND, 0)
            val targetTime = calendar.timeInMillis

            val delayTime = if (targetTime > currentTime) {
                targetTime - currentTime
            } else {
                targetTime + 24 * 60 * 60 * 1000 - currentTime
            }

            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                for (device in devices) {
                    if (device.id.contains(deviceFilter)) {
                        sendDeviceCommand(device.id, deviceCommand)
                    }
                }
            }, delayTime)
        }
    }

    public fun commandDeviceSuccess(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread {
                Toast.makeText(this, "Commande Envoyée", Toast.LENGTH_SHORT).show()
            }
        } else if (responseCode == 400) {
            runOnUiThread {
                Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
            }
        } else if (responseCode == 403) {
            runOnUiThread {
                Toast.makeText(this, "Accès interdit", Toast.LENGTH_SHORT).show()
            }
        } else if (responseCode == 500) {
            runOnUiThread {
                Toast.makeText(this, "Une erreur s’est produite au niveau du serveur", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendDeviceCommand(deviceId: String, command: String) {

        val commandData = CommandData(command=command)
        Api().post<CommandData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseId/devices/$deviceId/command",commandData,::commandDeviceSuccess,token)
        if (command == "STOP") {
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                loadDevices()
            }, 400)
        }
        if (deviceId.startsWith("Light")) {
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                loadDevices()
            }, 400)
        } else {
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                loadDevices()
            }, 8000)
        }
    }
}
