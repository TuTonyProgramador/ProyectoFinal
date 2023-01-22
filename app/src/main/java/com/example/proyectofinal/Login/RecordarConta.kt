package com.example.proyectofinal.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityRecordarContaBinding
import com.google.firebase.auth.FirebaseAuth

class RecordarConta : AppCompatActivity() {

    lateinit var binding: ActivityRecordarContaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordarContaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BRecordar.setOnClickListener {
            recuperarContra()
        }
    }

    private fun recuperarContra() {
        if(binding.IntEmail.text.isNotEmpty()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().sendPasswordResetEmail(
                binding.IntEmail.text.toString()
            )
                .addOnCompleteListener{
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful){
                        Toast.makeText(this,"Se ha mandado un correo, para cambiar la contraseña", Toast.LENGTH_SHORT).show()

                        // Accedemos a la pantalla PajarosActivity, que es la pantalla del programa donde va a mostrar los pajaros
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // sino avisamos el usuario que ocurrio un problema
                        Toast.makeText(this,"El correo  introducido no es correcto", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
        }

    }
}