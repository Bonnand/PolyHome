package com.adrien_bonnand.polyhome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.common.api.Api

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    public fun goToLogin(view: View)
    {
        finish();
    }

    fun register(view: View){

        val loginText = findViewById<EditText>(R.id.txtRegisterLogin);
        val passwordText = findViewById<EditText>(R.id.txtRegisterPassword);

        val registerData =
            RegisterData(
                login = loginText.text.toString(),
                password = passwordText.text.toString());

        Api().post<RegisterData>("https://polyhome.lesmoulinsdudev.com/api/users/register", registerData, ::registerSuccess)

    }



    private fun registerSuccess(responseCode : Int){
        if(responseCode==200){
            finish()
        }

        else if(responseCode==400){
            val registerMessage = findViewById<TextView>(R.id.registerMessage);
            Thread {
                runOnUiThread {
                    registerMessage.text="Les données fournies sont incorrectes"
                }
            }.start()
        }
        else if(responseCode==409){
            val registerMessage = findViewById<TextView>(R.id.registerMessage);
            Thread {
                runOnUiThread {
                    registerMessage.text="Le login est déjà utilisé par un autre compte"
                }
            }.start()
        }
        else if(responseCode==500){
            val registerMessage = findViewById<TextView>(R.id.registerMessage);
            Thread {
                runOnUiThread {
                    registerMessage.text="Une erreur s’est produite au niveau du serveur"
                }
            }.start()
        }
    }
}