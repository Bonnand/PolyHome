package com.example.androidtp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

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

        val nameText = findViewById<EditText>(R.id.txtRegisterName);
        val mailText = findViewById<EditText>(R.id.txtRegisterMail);
        val passwordText = findViewById<EditText>(R.id.txtRegisterPassword);

        val registerData =
            RegisterData(
                name = nameText.text.toString(),
                mail = mailText.text.toString(),
                password = passwordText.text.toString());

        Api().post<RegisterData>("https://mypizza.lesmoulinsdudev.com/register", registerData, ::registerSuccess)

    }



    private fun registerSuccess(responseCode : Int){
        if(responseCode==200){
            //finish()
            val intentLeave = Intent(
               this,
                LoginActivity::class.java
           )

           startActivity(intentLeave);
        }
    }
}