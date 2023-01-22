package com.example.proyectofinal.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.SplashActivity
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.example.proyectofinal.menu.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BIniciarSesion.setOnClickListener {
            login()
        }

        binding.BLlevarRegistro.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.RecordarContrasena.setOnClickListener {
            startActivity(Intent(this, RecordarConta::class.java))
        }

        /*db.collection("Usuarios")
            .whereEqualTo("Correo", binding.IntroEmail.text.toString())
            .get().addOnSuccessListener(){
                Glide.with(MainActivity.this).load("Imagen").into(binding.ImagenInicio)
            }*/
    }

    private fun login(){
        // Si el correo y el password no son campos vacios:
        if(binding.IntroEmail.text.isNotEmpty() && binding.IntroContrasena.text.isNotEmpty()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.IntroEmail.text.toString(),
                binding.IntroContrasena.text.toString()
            )
                .addOnCompleteListener{
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful){
                        // Accedemos a la pantalla PajarosActivity, que es la pantalla del programa donde va a mostrar los pajaros
                        val intent = Intent(this, PajarosActivity::class.java).apply {
                            putExtra("emailUsuario", binding.IntroEmail.text.toString())
                        }
                        startActivity(intent)
                        // Llamada al metodo patra limpiar el foco
                        clearFocus()
                    } else {
                        // sino avisamos el usuario que ocurrio un problema
                        Toast.makeText(this,"El correo o la contraseña introducida es incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
        }
    }

    //Al pulsar sobre el boton añadir, se limpia
    fun clearFocus() {
        binding.IntroEmail.setText("")
        binding.IntroContrasena.setText("")
    }
}