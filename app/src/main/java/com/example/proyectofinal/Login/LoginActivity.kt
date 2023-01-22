package com.example.proyectofinal.Login

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var imagen: ImageButton
    val db = FirebaseFirestore.getInstance()


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        // Devuelve la uri de la imagen seleccionada
            uri ->
        if(uri!=null) {
            // Seleccionamos la imagen
            imagen.setImageURI(uri)

            Log.d("Galeria", "La imagen se ha seleccionado correctamente")

        } else{
            Log.d("Galeria", "No se ha seleccionado ninguna imagen")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // imageButton
        imagen = binding.imagenB

        binding.BRegistrarse.setOnClickListener {
            Registro()
        }

        // Cuando pulsemos sobre el imageButton, se va a llamar al launcher (pickMedia) para que se lanze
        imagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }

    private fun Registro(){
        // Si el correo y el password no son campos vacios:
        if(binding.INombre.text.isNotEmpty() && binding.IApellidos.text.isNotEmpty() && binding.IEmail.text.isNotEmpty() && binding.IContrasena.text.isNotEmpty()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                binding.IEmail.text.toString(),
                binding.IContrasena.text.toString()
            )
                .addOnCompleteListener{
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful){
                        // Añadimos los datos del usuario en Firebase
                        db.collection("Usuarios").document(binding.IEmail.text.toString())
                            .set(mapOf("Nombre" to binding.INombre.text.toString(), "Apellidos" to binding.IApellidos.text.toString(), "Imagen" to binding.imagenB.toString()))

                        // Accedemos a la pantalla InicioActivity, para dar la bienvenida al usuario
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        // sino avisamos el usuario que ocurrio un problema
                        Toast.makeText(this,"El correo o la contraseña introducida es incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {

            Toast.makeText(this, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
        }
    }
}