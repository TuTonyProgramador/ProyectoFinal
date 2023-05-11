package com.example.proyectofinal.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.databinding.ActivityDatosCriadorBinding
import com.google.firebase.firestore.FirebaseFirestore

class DatosCriadorActivity : AppCompatActivity() {

    // Declarar la variable "binding" como una instancia de la clase "ActivityDatosCriadorBinding"
    lateinit var binding: ActivityDatosCriadorBinding

    // Inicializar una instancia de la base de datos de Firebase Firestore
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar la vista "ActivityDatosCriadorBinding" para poder acceder a sus elementos
        binding = ActivityDatosCriadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recoger dato del numero de criador
        var numeroCriador = intent.getStringExtra("NumCriador")

        // Realizar una consulta en la colección "Criadores" filtrando por el campo "NumeroCriador" que tenga el valor de "numeroCriador"
        db.collection("Criadores")
            .whereEqualTo("NumeroCriador", numeroCriador)
            .get().addOnSuccessListener() {
                // Para cada documento en los resultados de la consulta, establecer el valor de los elementos en la vista correspondientes a los valores almacenados en el documento
                it.forEach {
                    binding.ConsultarNombre.setText(it.get("Nombre") as String?)
                    binding.ConsultarApellidos.setText(it.get("Apellidos") as String?)
                    binding.ConsultarFederacion.setText(it.get("Federacion") as String?)
                    binding.ConsultarLocalidad.setText(it.get("Localidad") as String?)
                    binding.ConsultarAsociacion.setText(it.get("Asociacion") as String?)
                }
            }
    }
}