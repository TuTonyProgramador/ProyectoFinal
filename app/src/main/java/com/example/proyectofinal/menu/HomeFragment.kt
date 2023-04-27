package com.example.proyectofinal.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import com.example.proyectofinal.adapter.DatosAve
import com.example.proyectofinal.adapter.ProyectoAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var recycler: RecyclerView? = null
    private var listaAves = ArrayList<DatosAve>()

    //email = arguments?.getString("Email")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Te carga el recycler
        var vista: View = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar mi lista
        listaAves = ArrayList()
        recycler = vista.findViewById(R.id.Recycler)
        recycler?.layoutManager = LinearLayoutManager(context)

        // Llamada al metodo para cargar los datos de los canarios
        cargarDatos()

        recycler?.adapter = ProyectoAdapter(listaAves)


        return vista
    }

    // Cargar datos de los canarios
    fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        // Filtrar documentos por el campo "usuario" que tenga el valor del email del usuario actual
        db.collection("Canarios")
            .whereEqualTo("Usuario", currentUserEmail)
            .get()
            .addOnSuccessListener { cargar ->
                for (canarios in cargar) {
                    var canario = canarios.toObject(DatosAve::class.java)
                    listaAves.add(canario)
                }
                var adapter = ProyectoAdapter(listaAves)
                recycler?.adapter = adapter
            }
            .addOnFailureListener {
                Log.d("Cargar", "Error en la obtención del canario")
            }
    }


}