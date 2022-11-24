package com.example.aulasapp
import android.annotation.SuppressLint
import android.app.Application
import com.example.aulasapp.Reporte.Companion.listaActividades
import java.text.SimpleDateFormat
import java.util.*

class Reporte : Application() {

    companion object {
        @JvmField
        var listaActividades: MutableList<String> = mutableListOf()
    }
}

fun agregarReporte(report:String){
    listaActividades.add("$report ${getCurrentDate()}")
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDate():String{
    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    return sdf.format(Date())
}

