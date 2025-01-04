package com.adrien_bonnand.polyhome.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.adrien_bonnand.polyhome.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    public fun goToLogin(view: View)
    {
        finish();
    }

    public fun register(view: View){

        val loginText = findViewById<EditText>(R.id.registerLoginEntry);
        val passwordText = findViewById<EditText>(R.id.registerPasswordEntry);

        val registerData =
            RegisterData(
                login = loginText.text.toString(),
                password = passwordText.text.toString());

        com.adrien_bonnand.polyhome.Api().post<RegisterData>("https://polyhome.lesmoulinsdudev.com/api/users/register", registerData, ::registerSuccess)

    }

    private fun registerSuccess(responseCode : Int){
        if(responseCode==200){
            finish()
        }

        else if(responseCode==400){
        val registerMessage = findViewById<TextView>(R.id.registerMessage);
            runOnUiThread {
                registerMessage.text="Les données fournies sont incorrectes"
            }
        }
        else if(responseCode==409){
        val registerMessage = findViewById<TextView>(R.id.registerMessage);
            runOnUiThread {
                registerMessage.text="Le login est déjà utilisé par un autre compte"
            }
        }
        else if(responseCode==500){
        val registerMessage = findViewById<TextView>(R.id.registerMessage);
            runOnUiThread {
                registerMessage.text="Une erreur s’est produite au niveau du serveur"
            }
        }
    }
}