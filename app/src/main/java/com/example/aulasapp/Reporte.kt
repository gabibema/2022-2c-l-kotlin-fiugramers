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

    private var mStor: StorageReference= FirebaseStorage.getInstance().reference
    companion object {
        @JvmField
        var listaActividades: MutableList<String> = mutableListOf()
    }

    fun crearReporte() {
        val logRef = mStor.child("log.txt")
        ejecutarReporte(logRef)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ejecutarReporte(log: StorageReference) {
        GlobalScope.launch {
            while(true){
                delay(10000L)
                val localLog = File.createTempFile("log", "txt")
                log.getFile(localLog).addOnCompleteListener{
                    agregarReporte(localLog)
                    log.putStream(localLog.inputStream())
                    localLog.delete()
                }
            }
        }
    }

    private fun agregarReporte(localStream: File) {
        if(listaActividades.isEmpty()) return
        while(listaActividades.isNotEmpty()){
            localStream.appendText("${listaActividades.first()}\n",)
            listaActividades.removeFirst()
        }
    }
}

