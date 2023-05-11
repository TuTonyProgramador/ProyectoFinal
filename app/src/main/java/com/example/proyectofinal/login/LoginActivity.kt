package com.example.proyectofinal.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflamos el layout de la actividad utilizando el método inflate del objeto binding
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Establecemos el layout inflado como contenido de la actividad
        setContentView(binding.root)

        // Pulsar boton de registrarse
        binding.BRegistrarse.setOnClickListener {
            // LLamada al metodo de registrarse
            Registro()
        }

    }

    // Metodo para el registro de un nuevo usuario
    private fun Registro() {
        // Si el correo y el password no son campos vacios:
        if (binding.INombre.text.isNotEmpty() && binding.IApellidos.text.isNotEmpty() && binding.IEmail.text.isNotEmpty() && binding.IContrasena.text.isNotEmpty() && validarEmail()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                binding.IEmail.text.toString(),
                binding.IContrasena.text.toString()
            )
                .addOnCompleteListener {
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful) {
                        // Añadimos los datos del usuario en Firebase
                        db.collection("Usuarios").document(binding.IEmail.text.toString())
                            .set(
                                mapOf(
                                    "Nombre" to binding.INombre.text.toString(),
                                    "Apellidos" to binding.IApellidos.text.toString()
                                )
                            )

                        // Accedemos a la pantalla InicioActivity, para dar la bienvenida al usuario
                        val intent = Intent(this, MainActivity::class.java)

                        startActivity(intent)

                    } else {
                        // sino avisamos el usuario que ocurrio un problema
                        Toast.makeText(this, "El correo o la contraseña introducida es incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Puede ser que algun campo este vacio o sea incorrecto", Toast.LENGTH_SHORT).show()
        }
    }

    // Funcion para validar email que sea correcto
    fun validarEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(binding.IEmail.text.toString()).matches()
    }
}