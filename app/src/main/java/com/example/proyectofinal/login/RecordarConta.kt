package com.example.proyectofinal.login

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

        // Inflamos el layout de la actividad utilizando el método inflate del objeto binding
        binding = ActivityRecordarContaBinding.inflate(layoutInflater)

        // Establecemos el layout inflado como contenido de la actividad
        setContentView(binding.root)

        // Boton de recordar la contraseña
        binding.BRecordar.setOnClickListener {
            // Llamada al metodo de recuperar la contraseña
            recuperarContra()
        }
    }

    // Metodo para recuperar la contraseña
    private fun recuperarContra() {
        // Verificamos que el campo de correo electrónico no esté vacío
        if (binding.IntEmail.text.isNotEmpty()) {
            // Iniciamos sesión con el método sendPasswordResetEmail de FirebaseAuth, que envía un correo electrónico para restablecer la contraseña
            FirebaseAuth.getInstance().sendPasswordResetEmail(
                binding.IntEmail.text.toString()
            )
                .addOnCompleteListener {
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful) {
                        // Mostramos un mensaje de confirmación
                        Toast.makeText(this, "Se ha mandado un correo, para cambiar la contraseña", Toast.LENGTH_SHORT ).show()

                        // Accedemos a la pantalla PajarosActivity, que es la pantalla del programa donde va a mostrar los pajaros
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // Si la autenticación no tuvo éxito, avisamos al usuario que ocurrió un problema
                        Toast.makeText(this, "El correo introducido no es correcto", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // Si el campo de correo electrónico está vacío, avisamos al usuario
            Toast.makeText(this, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
        }
    }
}
