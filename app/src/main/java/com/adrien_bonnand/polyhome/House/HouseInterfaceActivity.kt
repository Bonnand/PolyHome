package com.adrien_bonnand.polyhome.House

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.Device.DevicesListActivity
import com.adrien_bonnand.polyhome.User.ExistingUserData
import com.adrien_bonnand.polyhome.User.GuestData
import com.adrien_bonnand.polyhome.R
import com.adrien_bonnand.polyhome.User.UserAdapter

class HouseInterfaceActivity : AppCompatActivity() {
    private val houseUsers: ArrayList<HouseUserData> = ArrayList() // List of users of a house
    private val existingUsers: ArrayList<String> = ArrayList() // List of existing users
    private lateinit var userAdapter: UserAdapter
    private var token: String? = null
    private var selectedHouse: String? = null // Selected house in HousesListActivity (=houseOwnerNumber initially)
    private var selectedUser: String = "null" // Selected user in the spinner
    private var houseOwnerNumber: String? = null // ID number of the owner house

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_house_interface)

        token=intent.getStringExtra("token")
        selectedHouse=intent.getStringExtra("selectedHouse")
        houseOwnerNumber=intent.getStringExtra("houseOwnerNumber")

        val txt = findViewById<TextView>(R.id.houseNumber);
        txt.text=selectedHouse

        val linearLayout2 = findViewById<LinearLayout>(R.id.linearLayout2)
        if (selectedHouse == houseOwnerNumber) { // Permit to add/delete guest only if it's his own house
            linearLayout2.visibility = View.VISIBLE
            loadExistingUsers()
        }

        initUsersListView()
        loadUsers()
    }

    private fun initUsersListView() {
        val listView = findViewById<ListView>(R.id.usersList)
        userAdapter = UserAdapter(this, houseUsers)
        listView.adapter = userAdapter
    }

    private fun loadUsers() {
        Api().get<ArrayList<HouseUserData>>("https://polyhome.lesmoulinsdudev.com/api/houses/$selectedHouse/users", ::loadUsersSuccess,token)
    }

    private fun loadUsersSuccess(responseCode: Int, loadedUsers: ArrayList<HouseUserData>?) {
        if (responseCode == 200 && loadedUsers != null) {
            houseUsers.clear()
            houseUsers.addAll(loadedUsers)
            runOnUiThread {
                userAdapter.notifyDataSetChanged()
                val houseNumber = findViewById<TextView>(R.id.houseNumber)
                if(selectedHouse==houseOwnerNumber) { // Permit to differentiate your home from others
                    houseNumber.text = "Ma maison ($selectedHouse)"
                }
                else{
                    houseNumber.text = "Maison numéro : $selectedHouse"
                }
            }
        }
    }

    private fun loadExistingUsers() {
        Api().get<ArrayList<ExistingUserData>>("https://polyhome.lesmoulinsdudev.com/api/users", ::loadExistingUsersSuccess)
    }

    private fun loadExistingUsersSuccess(responseCode: Int, loadedUsers: ArrayList<ExistingUserData>?) {
        if (responseCode == 200 && loadedUsers!=null) {
            existingUsers.clear()
            existingUsers.addAll(loadedUsers.map { it.login })
            val usersView = findViewById<Spinner>(R.id.existingUsersList)
            runOnUiThread {
                usersView.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, existingUsers)
                usersView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedUser = existingUsers[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }
    }

    public fun addGuest(view: View)
    {
        val guestData = GuestData(userLogin=selectedUser);

        Api().post<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseOwnerNumber/users",guestData, ::addGuestSuccess ,token)
    }

    fun deleteGuest(view: View)
    {
        val guestData = GuestData(userLogin=selectedUser);

        Api().delete<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/$houseOwnerNumber/users",guestData, ::deleteGuestSuccess ,token)
    }

    private fun addGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
                runOnUiThread {
                    loadUsers()
                    Toast.makeText(this, "Invité ajouté", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 400) {
                runOnUiThread {
                    Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 403) {
                runOnUiThread {
                    Toast.makeText(this, "Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 500) {
                runOnUiThread {
                    Toast.makeText(this, "Une erreur s’est produite au niveau du serveur", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun deleteGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
                runOnUiThread {
                    loadUsers()
                    Toast.makeText(this, "Invité supprimé", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 400) {
                runOnUiThread {
                    Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 403) {
                runOnUiThread {
                    Toast.makeText(this, "Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)", Toast.LENGTH_SHORT).show()
                }
        } else if (responseCode == 500) {
                runOnUiThread {
                    Toast.makeText(this, "Une erreur s’est produite au niveau du serveur", Toast.LENGTH_SHORT).show()
                }
        }
    }

    public fun switchHouse(view: View) {
        val intentLeave = Intent(
            this,
            HousesListActivity::class.java
        )

        intentLeave.putExtra("token", token)
        intentLeave.putExtra("houseOwnerNumber", houseOwnerNumber) // Permit to save houseOwnerNumber
        startActivity(intentLeave);
    }

    public fun controlHouse(view: View) {
        val intentLeave = Intent(
            this,
            DevicesListActivity::class.java
        )

        intentLeave.putExtra("token", token)
        intentLeave.putExtra("houseId", selectedHouse)
        startActivity(intentLeave);
    }
}
