package com.example.proyectofinal.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Boton para iniciar seccion
        binding.BIniciarSesion.setOnClickListener {
            login { userName ->
                val intent = Intent(this, PajarosActivity::class.java)
                intent.putExtra("emailUsuario", binding.IntroEmail.text.toString())
                intent.putExtra("Nombre", userName)
                startActivity(intent)
            }
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

    private fun login(onUserLoaded: (userName: String) -> Unit) {
        // Si el correo y el password no son campos vacios:
        if (binding.IntroEmail.text.isNotEmpty() && binding.IntroContrasena.text.isNotEmpty()) {
            // Iniciamos sesión con el método signIn y enviamos a Firebase el correo y la contraseña
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.IntroEmail.text.toString(),
                binding.IntroContrasena.text.toString()
            )
                .addOnCompleteListener { task ->
                    // Si la autenticación tuvo éxito:
                    if (task.isSuccessful) {
                        var nombre: String
                        var apellidos: String
                        var userName: String

                        val auth = FirebaseAuth.getInstance()
                        val db = FirebaseFirestore.getInstance()

                        eliminarNotasAnterioresAlDiaActual()

                        auth.currentUser?.let {
                            db.collection("Usuarios")
                                //.whereEqualTo("id", auth.currentUser?.email)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (usuario in documents) {
                                        if (usuario.id == auth.currentUser?.email){
                                            nombre = usuario.getString("Nombre") ?: ""
                                            apellidos = usuario.getString("Apellidos") ?: ""
                                            userName = nombre + " " + apellidos
                                            onUserLoaded(userName) // Llama a la función lambda con el nombre de usuario

                                            clearFocus()

                                            finish()
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("Usuario", "Error al obtener el usuario", exception)
                                }
                        }
                    } else {
                        Toast.makeText(this, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show()
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
}