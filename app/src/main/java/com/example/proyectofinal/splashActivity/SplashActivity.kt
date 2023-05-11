package com.example.proyectofinal.splashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.proyectofinal.login.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Muestra una pantalla de presentación utilizando la API SplashScreen en dispositivos compatibles.
        val screenSplash = installSplashScreen()

        // Llama al método onCreate de la superclase para crear la actividad y establecer su estado.
        super.onCreate(savedInstanceState)

        // Establece la condición para mantener la pantalla de presentación en la pantalla del dispositivo.
        screenSplash.setKeepOnScreenCondition { true }

        // Espera 4 segundos utilizando la función sleep de la clase Thread.
        Thread.sleep(4000)

        // Crea un Intent para iniciar la actividad MainActivity.
        val intent = Intent(this, MainActivity::class.java)

        // Inicia la actividad MainActivity utilizando el Intent creado anteriormente.
        startActivity(intent)

        // Finaliza la actividad actual (SplashActivity).
        finish()
    }
}