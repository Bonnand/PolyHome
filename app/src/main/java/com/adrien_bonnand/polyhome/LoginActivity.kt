package com.example.androidtp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

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

        val mailText = findViewById<EditText>(R.id.txtMail);
        val passwordText = findViewById<EditText>(R.id.txtPassword);


        val loginData =
            LoginData(
                mail = mailText.text.toString(),
                password = passwordText.text.toString());

        Api().post<LoginData,String>("https://mypizza.lesmoulinsdudev.com/auth", loginData, ::loginSuccess)
    }


    private fun loginSuccess (responseCode : Int, token : String?){
        if(responseCode==200){
            //finish()
            val intentLeave = Intent(
                this,
                OrdersActivity::class.java
            )

            intentLeave.putExtra("token",token)

            startActivity(intentLeave);
        }
    }
}