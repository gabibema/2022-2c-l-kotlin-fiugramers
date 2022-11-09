package com.example.aulasapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
//import com.google.firebase.firestore.ktx.firestore

class HomeActivity : AppCompatActivity() {

    private lateinit var aulas :ArrayList<Aula>
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter : CostumAdapter
    private lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        generarAulas()

        logout = findViewById<Button>(R.id.logout)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        recyclerView.layoutManager = LinearLayoutManager(this)

        aulas = arrayListOf()

        adapter = CostumAdapter(aulas, onClickListener = {id,posicion -> reservarAula(id,posicion)})

        recyclerView.adapter = adapter
        ingresarHome();
    }

    private fun reservarAula(id:String,posicion:Int) {
        val aula = db.collection("aulas").document(id)
        aula.update("estado",false)
        println("Ingreso a reservar aula")
        adapter.notifyItemChanged(posicion) // esto deberia refrescar pero no lo hace ¿¿¿¿¿QUE HAGO MAL??????

    }


    private fun ingresarHome(){
        logout.setOnClickListener{
            //generarAulas() // genera las aulas en la bd
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))
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

    private fun generarAulas() {
        db = FirebaseFirestore.getInstance()
        db.collection("aulas")
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    for(aula:DocumentChange in value?.documentChanges!!){
                        if(aula.type == DocumentChange.Type.ADDED){
                            if(aula.document.data.get("estado") == true){
                                aulas.add(Aula(aula.document.id,"Disponible"))
                            }else{
                                aulas.add(Aula(aula.document.id,"Ocupado"))
                            }
                    }
                        adapter.notifyDataSetChanged()
                }

            }
            })
    }
}