package com.example.aulasapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegistrarActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var guardar:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        mAuth = FirebaseAuth.getInstance()
        guardar = findViewById<Button>(R.id.guardar)
        ingresarRegistro()
    }

    private fun ingresarRegistro(){
        guardar.setOnClickListener {

            var email = findViewById<TextView>(R.id.email2).text.toString()
            var password = findViewById<TextView>(R.id.password2).text.toString()
            var nombre = findViewById<TextView>(R.id.nombre).text.toString()
            var apellido = findViewById<TextView>(R.id.apellido).text.toString()
            var alumno = findViewById<RadioButton>(R.id.alumno)
            var profesor = findViewById<RadioButton>(R.id.profesor)

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
                            ingresarHome()
                        } else {
                            reiniciarCampos()
                            mostrarError()
                        }
                    })
            }else{
                reiniciarCampos()
                mostrarError()
            }
        }
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

    private fun mostrarError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Debe completar todos los datos con un mail no registrado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun ingresarHome(){
        val homeIntent = Intent(this,HomeActivity::class.java)
        startActivity(homeIntent)
    }
}