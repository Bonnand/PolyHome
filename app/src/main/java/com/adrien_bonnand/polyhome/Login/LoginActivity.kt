package com.adrien_bonnand.polyhome.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.adrien_bonnand.polyhome.Api
import com.adrien_bonnand.polyhome.House.HouseInterfaceActivity
import com.adrien_bonnand.polyhome.R

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

        val loginText = findViewById<EditText>(R.id.loginEntry);
        val passwordText = findViewById<EditText>(R.id.passwordEntry);


        val loginData =
            LoginData(
                login = loginText.text.toString(),
                password = passwordText.text.toString());

        Api().post<LoginData, TokenData>("https://polyhome.lesmoulinsdudev.com/api/users/auth", loginData, ::loginSuccess)

    }

    private fun loginSuccess (responseCode : Int, tokenData : TokenData?){
        if(responseCode==200 && tokenData!=null){
            val intentLeave = Intent(
                this,
                HouseInterfaceActivity::class.java
            )

            intentLeave.putExtra("token",tokenData.token)
            intentLeave.putExtra("selectedHouse","81")
            startActivity(intentLeave);
        }
        else if(responseCode==400){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
                runOnUiThread {
                    loginMessage.text="Les données fournies sont incorrectes"
                }
        }
        else if(responseCode==401){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
                runOnUiThread {
                    loginMessage.text="Aucun utilisateur ne correspond aux identifiants donnés"
                }
        }
        else if(responseCode==500){
            val loginMessage = findViewById<TextView>(R.id.loginMessage);
                runOnUiThread {
                    loginMessage.text="Une erreur s’est produite au niveau du serveur"
                }
        }
    }
}