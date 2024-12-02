package com.adrien_bonnand.polyhome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
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


        Api().post<LoginData,TokenData>("https://polyhome.lesmoulinsdudev.com/api/users/auth", loginData, ::loginSuccess)

    }


    private fun loginSuccess (responseCode : Int, tokenData : TokenData?){
        if(responseCode==200 && tokenData!=null){
            val intentLeave = Intent(
                this,
                HousesListActivity::class.java
            )

            intentLeave.putExtra("token",tokenData.token)
            startActivity(intentLeave);
        }
        else if(responseCode==400){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
            Thread {
                runOnUiThread {
                    loginMessage.text="Les données fournies sont incorrectes"
                }
            }.start()
        }
        else if(responseCode==401){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
            Thread {
                runOnUiThread {
                    loginMessage.text="Aucun utilisateur ne correspond aux identifiants donnés"
                }
            }.start()
        }
        else if(responseCode==500){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
            Thread {
                runOnUiThread {
                    loginMessage.text="Une erreur s’est produite au niveau du serveur"
                }
            }.start()
        }
    }
}