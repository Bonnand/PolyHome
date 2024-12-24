package com.adrien_bonnand.polyhome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener

class HousesListActivity : AppCompatActivity() {
    private val houses: ArrayList<House> = ArrayList()
    private val users: ArrayList<String> = ArrayList()
    private lateinit var housesAdapter: HouseAdapter
    private var selectedUser: String = "defaultUser"


    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_houses_list)
        token=intent.getStringExtra("token")
        initHousesListView()
        loadUsers()
        loadHouses()
    }

    override fun onResume() {
        super.onResume()
        loadHouses()
    }

    public fun loadUsers() {
        Api().get<ArrayList<UserData>>("https://polyhome.lesmoulinsdudev.com/api/users", ::loadUsersSuccess)
    }

    public fun loadUsersSuccess(responseCode: Int, loadedUsers: ArrayList<UserData>?) {
        if (responseCode == 200 && loadedUsers!=null) {
            users.clear()
            users.addAll(loadedUsers.map { it.login })
                val usersView = findViewById<Spinner>(R.id.lstUsers)
            runOnUiThread {
                usersView.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
                usersView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedUser = users[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }
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
                val idHouse = findViewById<TextView>(R.id.idHouse)
                idHouse.text="Identifiant de la maison : " + houses[0].houseId.toString();
            }

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
            val house = getItem(position)

            if (house.owner) {
                val emptyView = View(context)
                emptyView.layoutParams = AbsListView.LayoutParams(0, 0)
                return emptyView
            }

            val rowView = inflater.inflate(R.layout.show_houses, parent, false)
            val houseIdText = rowView.findViewById<TextView>(R.id.textHouseId)
            val ownerText = rowView.findViewById<TextView>(R.id.textOwner)

            ownerText.text = "Membre"
            houseIdText.text = house.houseId.toString()

            return rowView
        }
    }

    public fun addGuest(view: View)
    {
        //il faut changer le layout
        //creer branch modification
        //et tout

        val guestData = GuestData(userLogin=selectedUser);

        Api().post<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/81/users",guestData, ::addGuestSuccess ,token)
    }

    public fun deleteGuest(view: View)
    {
        val guestData = GuestData(userLogin=selectedUser);

        Api().delete<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/81/users",guestData, ::deleteGuestSuccess ,token)
    }

    private fun addGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Invité ajouté", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 400) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 403) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 500) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Une erreur s’est produite au niveau du serveur", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
    }


    private fun deleteGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Invité supprimé", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 400) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 403) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)", Toast.LENGTH_SHORT).show()
                }
            }.start()
        } else if (responseCode == 500) {
            Thread {
                runOnUiThread {
                    Toast.makeText(this, "Une erreur s’est produite au niveau du serveur", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
    }
}