package com.example.aulasapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRegistrar

class MainActivity : AppCompatActivity() {

    @SuppressLint("InvalidAnalyticsName", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val analisis = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("mensaje","Integracion Firebase")
        analisis.logEvent("Inicio Screen",bundle)

        var btn_login = findViewById<Button>(R.id.login)
        var btn_registrar = findViewById<Button>(R.id.registrar)
        var str_email = findViewById<TextView>(R.id.email)
        var str_password = findViewById<TextView>(R.id.password)

        ingresarLogin(btn_registrar,btn_login,str_email,str_password)
    }
    private fun ingresarLogin(registrar:Button,login:Button,email:TextView,password:TextView){
            title = "Inicio de pantalla"
            registrar.setOnClickListener{
                if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),
                        password.text.toString()).addOnCompleteListener{
                            if(it.isSuccessful){
                                ingresarHome()
                            }else{
                                print("Error!!!!!!")
                            }
                    }
                }
            }

            login.setOnClickListener{
                if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),
                        password.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            ingresarHome()
                        }else{
                            print("Error!!!!!!")
                        }
                    }
                }
            }
    }
    private fun ingresarHome(){
        val homeIntent = Intent(this,HomeActivity::class.java)
        startActivity(homeIntent)
    }

}