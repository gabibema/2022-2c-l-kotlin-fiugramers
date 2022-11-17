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
        println("email: $email")
        menubar = findViewById(R.id.bottomNavegationView)

        inicializarFragments()

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

    private fun inicializarFragments() {
        bundle = Bundle()
        bundle.putString("email", email)

        fragmentHome = HomeFragment()
        inicializarFragment(fragmentHome,bundle)

        fragmentReserva = ReservasFragment()
        inicializarFragment(fragmentReserva,bundle)

        fragmentPerfil = PerfilFragment()
        inicializarFragment(fragmentPerfil,bundle)
    }

    private fun inicializarFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmenteTransition = fragmentManager.beginTransaction()
        fragmenteTransition.replace(R.id.containerView,fragment)
        fragmenteTransition.commit()
    }
}