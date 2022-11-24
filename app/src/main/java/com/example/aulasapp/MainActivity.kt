package com.example.aulasapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.aulasapp.Reporte.Companion.listaActividades
import com.example.aulasapp.classes.Home
import com.example.aulasapp.classes.Error
import com.example.aulasapp.classes.Registro
import com.example.aulasapp.classes.RegistroGoogle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.io.File.createTempFile


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var usuarioGoogle: FirebaseUser
    private lateinit var nombreGoogle:String
    private lateinit var fotoGoogle: Uri
    private val db = Firebase.firestore
    private val GOOGLE_SIGIN = 100
    private var mStor: StorageReference= FirebaseStorage.getInstance().reference

    @SuppressLint("InvalidAnalyticsName", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val btn_login = findViewById<Button>(R.id.login)
        val btn_registrar = findViewById<Button>(R.id.registrar)
        val btn_google = findViewById<Button>(R.id.google_login)

        ingresarLogin(btn_registrar,btn_login,btn_google)
        crearReporte()
    }

    private fun crearReporte() {
        val logRef = mStor.child("log.txt")
        ejecutarReporte(logRef)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ejecutarReporte(log: StorageReference) {
        GlobalScope.launch {
            while(true){
                delay(10000L)
                val localLog = File.createTempFile("log", "txt")
                log.getFile(localLog).addOnCompleteListener{
                    agregarReporte(localLog)
                    log.putStream(localLog.inputStream())
                    localLog.delete()
                }
            }
        }
    }

    private fun agregarReporte(localStream: File) {
        if(listaActividades.isEmpty()) return
        while(listaActividades.isNotEmpty()){
            localStream.appendText("${listaActividades.first()}\n",)
            listaActividades.removeFirst()
        }
    }

    private fun mostrarError(){
        val error = Error()
        db.collection("usuarios").document(email).get().addOnSuccessListener { usuario ->
            if(usuario.exists())
                error.mostrar("Usuario y/o contraseña incorrectos",this)
            else
                error.mostrar("El usuario no existe. Debe registrarse",this)
        }.addOnFailureListener {
            error.mostrar("El usuario no existe. Debe registrarse",this)
        }
    }

    private fun mostrarPantalla(it: Task<AuthResult>){
        if(it.isSuccessful){
            val home = Home(email)
            home.ingresar(this)
        }else{
            mostrarError()
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
                    agregarReporte("Inicio de sesión del usuario $email")
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
            usuario.signOut()
            startActivityForResult(usuario.signInIntent,100)
       }
    }

    private fun ingresarRegistro(){
        val registro = Registro()
        registro.ingresar(this)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if(requestCode == GOOGLE_SIGIN){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val cuenta = task.getResult(ApiException::class.java)

                if(cuenta!=null){
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken,null)

                    email = cuenta.email.toString()
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        usuarioGoogle = Firebase.auth.currentUser!!
                        db.collection("usuarios").document(email)
                            .addSnapshotListener { snapshot, _ ->
                                if (snapshot != null && snapshot.exists()) {
                                    listaActividades.add("Inicio de sesión del usuario $email")
                                    mostrarPantalla(it)
                                } else {
                                    ingresarRegistroGoogle()
                                }
                       }
                    }
                }
            }

        }catch (e:ApiException){
            val error = Error()
            error.mostrar("Se produjo una falla al iniciar con Google",this)
            Log.w("aaa", "Google sign in failed", e)
        }
    }


    private fun ingresarRegistroGoogle() {
        usuarioGoogle.let {
            nombreGoogle = usuarioGoogle.displayName.toString()
            fotoGoogle = usuarioGoogle.photoUrl!!
        }
        val googleSignIn = RegistroGoogle(nombreGoogle,email)

        googleSignIn.ingresar(this)
    }
}