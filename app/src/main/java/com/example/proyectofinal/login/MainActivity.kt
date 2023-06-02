package com.example.proyectofinal.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var contraseniavisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.eye.setOnClickListener{
            verContrasenia()
        }

    }

    private fun login() {
        // Si el correo y el password no son campos vacios:
        if (binding.IntroEmail.text.isNotEmpty() && binding.IntroContrasena.text.isNotEmpty()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.IntroEmail.text.toString(),
                binding.IntroContrasena.text.toString()
            )
                .addOnCompleteListener {
                    // Si la autenticación tuvo éxito:
                    if (it.isSuccessful) {

                        eliminarNotasAnterioresAlDiaActual()

                        // Accedemos a la pantalla PajarosActivity, que es la pantalla del programa donde va a mostrar los pajaros
                        val intent = Intent(this, PajarosActivity::class.java).apply {
                            putExtra("emailUsuario", binding.IntroEmail.text.toString())
                        }
                        startActivity(intent)
                        // Llamada al metodo patra limpiar el foco
                        clearFocus()
                    } else {
                        // sino avisamos el usuario que ocurrio un problema
                        Toast.makeText(this, "El correo o la contraseña introducida es incorrecta", Toast.LENGTH_SHORT).show()
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

    // Funcion para eliminar las notas de dias anteriores al actual que no elimine el usuario
    private fun eliminarNotasAnterioresAlDiaActual() {
        val auth = FirebaseAuth.getInstance() // Obtiene la instancia de FirebaseAuth
        var db = FirebaseFirestore.getInstance() // Obtiene la instancia de FirebaseFirestore
        val currentDate = Calendar.getInstance() // Obtiene la fecha actual utilizando Calendar
        val dia: Int = currentDate.get(Calendar.DAY_OF_MONTH) // Obtiene el día actual
        val mes: Int = currentDate.get(Calendar.MONTH) // Obtiene el mes actual
        val anio: Int = currentDate.get(Calendar.YEAR) // Obtiene el año actual
        var fechaActual = "" // Variable para almacenar la fecha actual en formato de cadena de texto

        currentDate.set(anio, mes, dia) // Configura la fecha actual en el objeto Calendar
        fechaActual = dia.toString() + mes.toString() + anio.toString() // Crea la cadena de la fecha actual sin separadores

        auth.currentUser.let {// Verifica que haya un usuario autenticado
            db.collection("Eventos")
                .whereLessThan("fecha", fechaActual)// Consulta documentos con campo "fecha" menor que fechaActual
                .get()
                .addOnSuccessListener { documents -> // Maneja el resultado exitoso de la consulta
                    val batch = db.batch() // Crea un lote de escritura en lotes
                    for (document in documents) { // Itera sobre los documentos obtenidos
                        val notaId = document.getString("id") // Obtiene el valor del campo "id" como cadena de texto
                        notaId?.let {
                            val notaRef = db.collection("Eventos").document(it) // Crea una referencia al documento correspondiente
                            batch.delete(notaRef)// Agrega una operación de eliminación al lote
                        }
                    }
                    if (documents.size() > 0) {
                        batch.commit() // Confirma y aplica las eliminaciones en lotes si hay documentos para eliminar
                    }
                }
                .addOnFailureListener { exception -> // Maneja la excepción si la consulta o el proceso de eliminación fallan
                    Toast.makeText(this, "Error al obtener las notas", Toast.LENGTH_SHORT).show()
                }
        }
    }
    override fun onBackPressed() {
        // Sobreescribe el método onBackPressed() de la Activity actual

        AlertDialog.Builder(this) // Crea un objeto AlertDialog.Builder con el contexto de la Activity actual
            .setTitle("Salir de la APP") // Establece el título del diálogo
            .setMessage("¿Estás seguro de que deseas salir?") // Establece el mensaje del diálogo
            .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                // Configura el botón positivo del diálogo con un texto y una acción al hacer clic
                finishAffinity() // Finaliza todas las Activities de la aplicación y cierra la aplicación
            }
            .setNegativeButton("No", null) // Configura el botón negativo del diálogo con un texto y sin ninguna acción
            .show() // Muestra el diálogo en la pantalla
    }


    private fun verContrasenia() {
        contraseniavisible = !contraseniavisible // Invierte el valor de la variable contraseniavisible

        if (contraseniavisible) {
            // Si la contraseña es visible
            binding.IntroContrasena.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            // Establece el tipo de entrada de texto como contraseña visible
            binding.eye.setImageResource(R.drawable.ver_password)
        } else {
            // Si la contraseña no es visible
            binding.IntroContrasena.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            // Establece el tipo de entrada de texto como contraseña oculta
            binding.eye.setImageResource(R.drawable.ver_password)
        }
    }


}