package com.example.aulasapp

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.aulasapp.classes.Home
import com.example.aulasapp.classes.Error
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegistroActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var guardar:Button
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var alumno:RadioButton
    private lateinit var profesor:RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        mAuth = FirebaseAuth.getInstance()
        guardar = findViewById<Button>(R.id.guardar)
        registrar()
    }


    private fun registrar(){
        guardar.setOnClickListener {

            obtenerPantalla()
            var error = Error()
            var mensaje = "Debe completar todos los datos con un mail no registrado"
            if (datosValidos(email, nombre, apellido, password)) {
                mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult?> { task ->
                        if (task.result.signInMethods?.isEmpty() == true) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                            if (profesor.isChecked) {
                                guardarBaseDatos(email, nombre, apellido, 1)
                            } else if (alumno.isChecked) {
                                guardarBaseDatos(email, nombre, apellido, 2)
                            }
                            val home = Home(email)
                            home.ingresar(this)
                        } else {
                            reiniciarCampos()
                            error.mostrar(mensaje,this)
                        }
                    })
            }else{
                reiniciarCampos()
                error.mostrar(mensaje,this)
            }
        }
    }

    private fun obtenerPantalla() {
        email = findViewById<TextView>(R.id.email2).text.toString()
        password = findViewById<TextView>(R.id.password2).text.toString()
        nombre = findViewById<TextView>(R.id.nombre).text.toString()
        apellido = findViewById<TextView>(R.id.apellido).text.toString()
        alumno = findViewById<RadioButton>(R.id.alumno)
        profesor = findViewById<RadioButton>(R.id.profesor)
    }


    private fun datosValidos(email:String,nombre:String,apellido:String, password:String): Boolean {
        return mailValido(email) && nombre.isNotEmpty() && apellido.isNotEmpty() && passwordValida(password)
    }

    private fun passwordValida(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }

    private fun mailValido(mail: String): Boolean {
        return mail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    private fun guardarBaseDatos(email:String,nombre:String,apellido:String,rol:Int){
        println("Se graba bd")
        db.collection("usuarios")
            .document(email).set(
                hashMapOf(
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "rol" to rol
                )
            )
    }

    private fun reiniciarCampos(){
        findViewById<TextView>(R.id.email2).text = ""
        findViewById<TextView>(R.id.password2).text = ""
        findViewById<TextView>(R.id.nombre).text = ""
        findViewById<TextView>(R.id.apellido).text = ""
        findViewById<RadioButton>(R.id.alumno).isChecked = false
        findViewById<RadioButton>(R.id.profesor).isChecked = false

    }
}