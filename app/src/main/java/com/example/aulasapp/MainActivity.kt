package com.example.aulasapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.aulasapp.fragment.DialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var usuario: FirebaseUser
    private lateinit var bundle: Bundle
    private lateinit var nombreGoogle:String
    private lateinit var fotoGoogle: Uri
    private val db = Firebase.firestore

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

    private fun meostrarError(){
        db.collection("usuarios").document(email).get().addOnSuccessListener { usuario ->
            if(usuario.exists())
                mensajeError("Usuario y/o contraseña incorrectos")
            else
                mensajeError("El usuario no existe. Debe registrarse")
        }.addOnFailureListener {
            mensajeError("El usuario no existe. Debe registrarse")
        }
    }
    private fun mensajeError(mensaje:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    private fun mostrarPantalla(it: Task<AuthResult>){
        if(it.isSuccessful){
            ingresarHome(email)
        }else{
            meostrarError()
        }
    }

    private fun ingresarLogin(registrar:Button,login:Button, google_login:Button){

        registrar.setOnClickListener{
            ingresarRegistro()//reemplaza a pedir mail y contraseña como antes
        }

        login.setOnClickListener{
            email = findViewById<TextView>(R.id.email).getText().toString()
            password = findViewById<TextView>(R.id.password).getText().toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(email,
                    password).addOnCompleteListener(this){
                    mostrarPantalla(it)
                }
            }
        }

       google_login.setOnClickListener{
            val googleConfiguracion = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val usuario = GoogleSignIn.getClient(this,googleConfiguracion)
           //usuario.signOut()
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

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            println("Entro0")

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            println("Entro1.1")
            val cuenta = task.getResult(ApiException::class.java)
            println("Entro1")
                if(cuenta!=null){
                    println("Entro2")
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken,null)
                    db.collection("users").document(cuenta.email.toString()).addSnapshotListener { snapshot, e ->
                      if(snapshot != null && snapshot.exists()){
                          println("Entro3")
                          FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener{
                              println("Entro4")
                              mostrarPantalla(it)
                         }
                      }
                    }
                    /*
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        email = cuenta.email.toString()
                        usuario = Firebase.auth.currentUser!!
                        val fecha = usuario.metadata?.creationTimestamp.toString()
                        val hoy = Calendar.getInstance().timeInMillis.toString()

                        db.collection("usuarios").document(email).get().addOnSuccessListener { document ->
                            if (!document.exists()) {
                                println("Ingresa por db")
                                if (fecha.substring(0, 5) == hoy.substring(0, 5))
                                    ingresarRegistroGoogle()
                            }
                        }


                    }*/


                }


        }catch (e:ApiException){
            mensajeError("Se produjo una falla al iniciar con Google")
            Log.w("aaa", "Google sign in failed", e)
        }
    }


    private fun ingresarRegistroGoogle() {
        usuario?.let {
            nombreGoogle = usuario.displayName.toString()
            fotoGoogle = usuario.photoUrl!!

        }
        val dialog = DialogFragment()
        bundle = Bundle()
        bundle.putString("email", email)
        bundle.putString("nombre",nombreGoogle)

        dialog.arguments = bundle

        dialog.show(supportFragmentManager,"dialog fragment")
        //replaceFragment(dialog)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmenteTransition = fragmentManager.beginTransaction()
        fragmenteTransition.replace(R.id.containerView,fragment)
        fragmenteTransition.commit()
    }
}