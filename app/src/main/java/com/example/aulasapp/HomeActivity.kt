package com.example.aulasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.databinding.ActivityMainBinding
import com.example.aulasapp.fragment.HomeFragment
import com.example.aulasapp.fragment.PerfilFragment
import com.example.aulasapp.fragment.ReservasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class HomeActivity : AppCompatActivity() {


    private lateinit var menubar:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        menubar = findViewById(R.id.bottomNavegationView)

        replaceFragment(HomeFragment())
        menubar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.item_perfil -> {
                    replaceFragment(PerfilFragment())
                    true
                }
                R.id.item_reservar -> {
                    replaceFragment(ReservasFragment())
                    true
                }
                else -> false
             }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmenteTransition = fragmentManager.beginTransaction()
        fragmenteTransition.replace(R.id.containerView,fragment)
        fragmenteTransition.commit()
    }
}
/*
    private fun generarAulas(descripcionAulas:TextView){
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


    }*/

 /*   private fun generarAulas(){
        val db = FirebaseFirestore.getInstance()

        for (i in 102..110){
            val aula = hashMapOf(
                "estado" to true
            )

            db.collection("aulas")
                .document(i.toString())
                .set(aula)
        }
    }*/