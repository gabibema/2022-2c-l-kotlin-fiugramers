package com.example.aulasapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var usuarioGoogle: FirebaseUser
    private lateinit var nombreGoogle:String
    private lateinit var fotoGoogle: Uri
    private lateinit var mStor: StorageReference
    private val db = Firebase.firestore

    @SuppressLint("InvalidAnalyticsName", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mStor = FirebaseStorage.getInstance().reference
        val btn_login = findViewById<Button>(R.id.login)
        val btn_registrar = findViewById<Button>(R.id.registrar)
        val btn_google = findViewById<Button>(R.id.google_login)

        ingresarLogin(btn_registrar,btn_login,btn_google)
        crearReporte()
    }

    private fun crearReporte() {
        var numero = 1
        var logRef = mStor.child("log.txt")
        ejecutarReporte(logRef)
    }

    private fun ejecutarReporte(localLog: StorageReference) {
        var numero = 1
        GlobalScope.launch {
            while(true){
                delay(2000L)
                var logStreamOut = OutputStreamWriter(openFileOutput("log.txt", Activity.MODE_PRIVATE))
                Reporte.listaActividades.add("Hello $numero")
                agregarReporte(logStreamOut)

                var logStreamIn = FileInputStream(File("log.txt"))
                localLog.putStream(logStreamIn)
                logStreamOut.flush()
                logStreamOut.close()
            }
        }
    }

    private fun agregarReporte(localStream: OutputStreamWriter) {
        if(Reporte.listaActividades.isEmpty() || localStream == null) return
        while(Reporte.listaActividades.isNotEmpty()){
            localStream.write("${Reporte.listaActividades.first()}")
            Reporte.listaActividades.removeFirst()

        }
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

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if(requestCode==100){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val cuenta = task.getResult(ApiException::class.java)

                if(cuenta!=null){
                    val credencial = GoogleAuthProvider.getCredential(cuenta.idToken,null)

                    email = cuenta.email.toString()
                    FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener {
                        usuarioGoogle = Firebase.auth.currentUser!!
                        db.collection("usuarios").document(email)
                            .addSnapshotListener { snapshot, e ->
                                if (snapshot != null && snapshot.exists()) {
                                    mostrarPantalla(it)
                                } else {
                                    ingresarRegistroGoogle()
                                }
                       }
                    }
                }
            }

        }catch (e:ApiException){
            mensajeError("Se produjo una falla al iniciar con Google")
            Log.w("aaa", "Google sign in failed", e)
        }
    }


    private fun ingresarRegistroGoogle() {
        usuarioGoogle?.let {
            nombreGoogle = usuarioGoogle.displayName.toString()
            fotoGoogle = usuarioGoogle.photoUrl!!

        }

        val registroGoogleIntent = Intent(this,RegistroGoogleActivity::class.java)
        registroGoogleIntent.putExtra("email",email)
        registroGoogleIntent.putExtra("nombre",nombreGoogle)
        startActivity(registroGoogleIntent)
    }
}