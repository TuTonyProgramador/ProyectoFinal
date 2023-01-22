package com.example.proyectofinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.Login.MainActivity

class SplashActivity : AppCompatActivity() {

    val contador:Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Para que no cargue doble el splash lo cargo del tiron
        Thread(Runnable {
            Thread.sleep(contador)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }).start()


    }
}