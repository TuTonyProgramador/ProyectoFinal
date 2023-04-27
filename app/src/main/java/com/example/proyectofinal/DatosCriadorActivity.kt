package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.databinding.ActivityDatosCriadorBinding
import com.google.firebase.firestore.FirebaseFirestore

class DatosCriadorActivity : AppCompatActivity() {

    lateinit var binding: ActivityDatosCriadorBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosCriadorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recoger dato del numero de criador
        var numeroCriador = intent.getStringExtra("NumCriador")

        db.collection("Criadores")
            .whereEqualTo("NumeroCriador", numeroCriador)
            .get().addOnSuccessListener() {
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