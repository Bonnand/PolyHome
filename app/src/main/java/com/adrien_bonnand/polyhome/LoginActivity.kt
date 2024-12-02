package com.adrien_bonnand.polyhome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.adrien_bonnand.polyhome.R
import com.google.android.gms.common.api.Api

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    public fun registerNewAccount(view: View)
    {
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

    public fun auth(view: View){

        val loginText = findViewById<EditText>(R.id.txtLogin);
        val passwordText = findViewById<EditText>(R.id.txtPassword);


        val loginData =
            LoginData(
                login = loginText.text.toString(),
                password = passwordText.text.toString());

        Api().post<LoginData,String>("https://polyhome.lesmoulinsdudev.com/api/users/auth", loginData, ::loginSuccess)
    }


    private fun loginSuccess (responseCode : Int, token : String?){
        if(responseCode==200){
            val intentLeave = Intent(
                this,
                HousesListActivity::class.java
            )

            intentLeave.putExtra("token",token)
            showErrorPopup(this, "login sucess.")
            startActivity(intentLeave);
        }
    }

    private fun showErrorPopup(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Erreur")
        builder.setMessage(errorMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert) // Icône d'alerte
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Fermer le pop-up
        }
        val dialog = builder.create()
        dialog.show()
    }
}