package com.example.aulasapp
import android.app.Application
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class Reporte : Application() {

    companion object {
        @JvmField
        var listaActividades: MutableList<String> = mutableListOf()
    }
}

