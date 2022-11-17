package com.example.aulasapp.classes

import android.content.Context
import androidx.appcompat.app.AlertDialog

class Error {
     fun mostrar(mensaje:String,view:Context){
        val builder = AlertDialog.Builder(view)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}