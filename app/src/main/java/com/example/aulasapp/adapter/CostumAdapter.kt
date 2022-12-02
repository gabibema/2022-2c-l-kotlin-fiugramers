package com.example.aulasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.aulasapp.aula.Aula
import com.example.aulasapp.R

class CostumAdapter(
    private val aulas: ArrayList<Aula>,
    private val rol:Number,
    private val onClickDelete: (String) -> Unit,
    private val id:Int,
    private val mensaje :String
):
    RecyclerView.Adapter<CostumAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val v = LayoutInflater.from(viewGroup.context)
                .inflate(id, viewGroup,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val aula : Aula = aulas[i]
        println(i)
        viewHolder.itemId.text = aula.id
        viewHolder.itemEstado.text = aula.estado

        if(viewHolder.itemButton.isVisible){
            viewHolder.itemButton.setOnClickListener {
                val text = mensaje + aula.id
                Toast.makeText(
                    viewHolder.itemButton.context,
                    text,
                    Toast.LENGTH_SHORT
                ).show()

                onClickDelete(aula.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return aulas.size
    }

    private fun esProfesor(rol: Number): Boolean {
        return rol.toInt() == 1
    }

    inner class  ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemId: TextView
        var itemEstado: TextView
        var itemButton: Button

        init {
            itemId = itemView.findViewById(R.id.item_id)
            itemEstado = itemView.findViewById(R.id.item_estadoDesc)

            itemButton = try {
                itemView.findViewById(R.id.item_reservar)
            }catch (e:Exception){
                itemView.findViewById(R.id.item_cancelar)
            }
            if(!esProfesor(rol)) {
                itemButton.visibility = INVISIBLE
            }
        }
    }
}