package com.example.aulasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore


import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var btn_logout = findViewById<Button>(R.id.logout)
        var texto = findViewById<TextView>(R.id.textView3)
        ingresarHome(btn_logout, texto);
    }

    private fun ingresarHome(logout:Button, texto:TextView){
        mostrarAulas(texto)
        logout.setOnClickListener{
            //generarAulas() // genera las aulas en la bd
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun mostrarAulas(descripcionAulas:TextView){
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var texto: String = ""

        db.collection("aulas")
            .get() //obtengo todos los datos
            .addOnSuccessListener { aulas ->
                for(aula in aulas){
                    if(aula.data.get("estado") == true){
                        texto += "AULA: ${aula.id} - ESTADO: Disponible\n"
                    }else{
                        texto += "AULA: ${aula.id} - ESTADO: Ocupado\n"
                    }

                }
                descripcionAulas?.text = texto
            }

            .addOnFailureListener { exception ->
                println("Error")
            }


    }

    private fun generarAulas(){
        val db = FirebaseFirestore.getInstance()

        for (i in 102..110){
            val aula = hashMapOf(
                "estado" to true
            )

            db.collection("aulas")
                .document(i.toString())
                .set(aula)
        }
    }
}