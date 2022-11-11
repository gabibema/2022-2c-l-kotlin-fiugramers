package com.example.aulasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.recyclerview.widget.RecyclerView

class CostumAdapter(
    private val aulas: ArrayList<Aula>,
    private val onClickListener: (String,Int,Button) -> Unit):
    RecyclerView.Adapter<CostumAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val aula : Aula = aulas[i]
        viewHolder.itemId.text = aula.id
        viewHolder.itemEstado.text = aula.estado
        if (viewHolder.itemEstado.text == "Disponible"){
            viewHolder.itemButton.visibility = View.VISIBLE
            viewHolder.itemButton.setOnClickListener{

                Toast.makeText(viewHolder.itemButton.context,
                    "Reservaste el aula ${aula.id}",
                    Toast.LENGTH_SHORT).show()


                onClickListener(aula.id,i,viewHolder.itemButton)
            }
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
            itemButton = itemView.findViewById(R.id.item_reservar)
        }
    }
}