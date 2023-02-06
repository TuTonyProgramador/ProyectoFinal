package com.example.proyectofinal.Login

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menu contectual ---------
        val imagen = binding.ImagenInicio
        registerForContextMenu(imagen)

        // Boton para iniciar seccion
        binding.BIniciarSesion.setOnClickListener {
            login()
        }

        // Boton para que nos lleve a la ventana del registro
        binding.BLlevarRegistro.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Boton para que nos lleve a la ventana del olvido de contraseña
        binding.RecordarContrasena.setOnClickListener {
            startActivity(Intent(this, RecordarConta::class.java))
        }

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

    // Menu contectual ---------
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual, menu)
        menu?.setHeaderTitle("Menu contextual")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.descargar_imagen -> {
                Toast.makeText(this, "Opcion descargar", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.ir_web -> {
                Toast.makeText(this, "Opcion web", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.eliminar_imagen -> {
                Toast.makeText(this, "Opcion eliminar", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}