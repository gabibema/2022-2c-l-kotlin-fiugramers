package com.example.aulasapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("InvalidAnalyticsName", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        val btn_login = findViewById<Button>(R.id.login)
        val btn_registrar = findViewById<Button>(R.id.registrar)
        val btn_google = findViewById<Button>(R.id.google_login)

        ingresarLogin(btn_registrar,btn_login,btn_google)
    }

    private fun mensajeError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario o contraseña incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
    private fun mensajeErrorRegistro(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("El usuario ya existe")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarPantalla(it: Task<AuthResult>, email:String){
        if(it.isSuccessful){
            ingresarHome(email)
        }else{
            mensajeError()
        }
    }

    private fun ingresarLogin(registrar:Button,login:Button, google_login:Button){

        registrar.setOnClickListener{
            ingresarRegistro()//reemplaza a pedir mail y contraseña como antes
        }

        login.setOnClickListener{
            var email = findViewById<TextView>(R.id.email).getText().toString()
            var password = findViewById<TextView>(R.id.password).getText().toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(email,
                    password).addOnCompleteListener(this){
                    mostrarPantalla(it, email)
                }
            }
        }

       google_login.setOnClickListener{
            val googleConfiguracion = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val usuario = GoogleSignIn.getClient(this,googleConfiguracion)
            usuario.signOut()
            startActivityForResult(usuario.signInIntent,100)
       }
    }


    private fun ingresarHome(email:String){
        val homeIntent = Intent(this,HomeActivity::class.java)
        homeIntent.putExtra("email",email)
        startActivity(homeIntent)
    }

    private fun ingresarRegistro(){
        val registroIntent = Intent(this,RegistrarActivity::class.java)
        startActivity(registroIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if(requestCode==100){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val cuenta = task.getResult(ApiException::class.java)

                if(cuenta!=null){
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken,null)

                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        var email = cuenta.email.toString()
                        mostrarPantalla(it, email)

                    }
                }

            }
        }catch (e:ApiException){
            mensajeError()
        }
    }
}