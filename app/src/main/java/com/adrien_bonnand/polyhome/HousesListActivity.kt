package com.adrien_bonnand.polyhome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.EditText
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
        val loginChoiced = findViewById<EditText>(R.id.loginChoiced);
        val userLogin= loginChoiced.text.toString();
        val guestData = GuestData(userLogin=userLogin);

        Api().post<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/81/users",guestData, ::addGuestSuccess ,token)
    }

    public fun deleteGuest(view: View)
    {
        val loginChoiced = findViewById<EditText>(R.id.loginChoiced);
        val userLogin= loginChoiced.text.toString();
        val guestData = GuestData(userLogin=userLogin);

        Api().delete<GuestData>("https://polyhome.lesmoulinsdudev.com/api/houses/81/users",guestData, ::deleteGuestSuccess ,token)
    }

    private fun addGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Invité ajouté"
                }
            }.start()
        } else if (responseCode == 400) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Les données fournies sont incorrectes"
                }
            }.start()
        } else if (responseCode == 403) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text ="Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)"
                }
            }.start()
        } else if (responseCode == 500) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Une erreur s’est produite au niveau du serveur"
                }
            }.start()
        }
    }


    private fun deleteGuestSuccess (responseCode : Int) {
        if (responseCode == 200) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Invité supprimé"
                }
            }.start()
        } else if (responseCode == 400) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Les données fournies sont incorrectes"
                }
            }.start()
        } else if (responseCode == 403) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text ="Accès interdit (token invalide ou ne correspondant pas au propriétaire de la maison)"
                }
            }.start()
        } else if (responseCode == 500) {
            val guestMessage = findViewById<TextView>(R.id.guestMessage);
            Thread {
                runOnUiThread {
                    guestMessage.text = "Une erreur s’est produite au niveau du serveur"
                }
            }.start()
        }
    }
}