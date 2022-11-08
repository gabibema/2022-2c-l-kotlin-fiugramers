package com.example.aulasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CostumAdapter(private val aulas:ArrayList<Aula>) : RecyclerView.Adapter<CostumAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val aula : Aula = aulas[i]
        viewHolder.itemId.text = aula.id
        viewHolder.itemEstado.text = aula.estado
    }

    override fun getItemCount(): Int {
        return aulas.size
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemId: TextView
        var itemEstado: TextView

        init {
            itemId = itemView.findViewById(R.id.item_id)
            itemEstado = itemView.findViewById(R.id.item_estadoDesc)
        }
    }
}