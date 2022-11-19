package com.example.aulasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.R

class ReservaAdapter(
    private val aulas: ArrayList<Aula>,
    private val onClickDelete: (String) -> Unit):
    RecyclerView.Adapter<ReservaAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout_reserva, viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val aula : Aula = aulas[i]
        println(aula)
        println(i)
        viewHolder.itemId.text = aula.id
        viewHolder.itemEstado.text = aula.estado
        viewHolder.itemButton.setOnClickListener{

            Toast.makeText(viewHolder.itemButton.context,
                "Reservaste el aula ${aula.id}",
                Toast.LENGTH_SHORT).show()

            onClickDelete(aula.id)

        }
    }

    override fun getItemCount(): Int {
        return aulas.size
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemId: TextView
        var itemEstado: TextView
        var itemButton: Button

        init {
            itemId = itemView.findViewById(R.id.item_id)
            itemEstado = itemView.findViewById(R.id.item_estadoDesc)
            itemButton = itemView.findViewById(R.id.item_cancelar)
        }
    }
}