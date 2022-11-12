package com.example.aulasapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.aulasapp.fragment.HomeFragment
import com.example.aulasapp.fragment.PerfilFragment
import com.example.aulasapp.fragment.ReservasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {


    private lateinit var menubar:BottomNavigationView
    private lateinit var email:String
    private lateinit var bundle: Bundle
    private lateinit var fragmentHome:Fragment
    private lateinit var fragmentReserva:Fragment
    private lateinit var fragmentPerfil:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        email = intent.getStringExtra("email").toString()

        menubar = findViewById(R.id.bottomNavegationView)

        bundle = Bundle()
        bundle.putString("email",email)

        fragmentHome = HomeFragment()
        fragmentHome.arguments = bundle

        fragmentReserva = ReservasFragment()
        fragmentReserva.arguments = bundle

        fragmentPerfil = ReservasFragment()
        fragmentPerfil.arguments = bundle

        replaceFragment(fragmentHome)

        menubar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_home -> {
                    replaceFragment(fragmentHome)
                    true
                }
                R.id.item_perfil -> {
                    replaceFragment(fragmentPerfil)
                    true
                }
                R.id.item_reservar -> {
                    replaceFragment(fragmentReserva)
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