package com.example.aulasapp
import android.app.Application

class Reporte : Application() {
    companion object {
        @JvmField
        var listaActividades: MutableList<String> = mutableListOf()
    }
}

