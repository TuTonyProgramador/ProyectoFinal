package com.example.proyectofinal.menu

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentModificarBinding
import com.google.firebase.firestore.FirebaseFirestore

class ModificarFragment : Fragment(R.layout.fragment_modificar) {
    private var _binding: FragmentModificarBinding? = null
    private val binding get() = _binding!!
    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
    _binding = FragmentModificarBinding.inflate(inflater, container, false)
    val view = binding.root

    binding.BModificar.setOnClickListener {
    if(!(binding.MAnioN.text.isNullOrEmpty() && binding.MNumAni.text.isNullOrEmpty() && binding.MNumC.text.isNullOrEmpty() && binding.MSexo.text.isNullOrEmpty() && binding.MTipoAve.text.isNullOrEmpty())) {
      db.collection("Canarios")
          /*.whereEqualTo(binding.MNumC.hashCode().toString())
          .get().addOnSuccessListener() {
              it.forEach {
                  binding.MNumC.setText(it.get("Numero_criador") as String?)
                  binding.MNumAni.setText(it.get("Num_anilla") as String?)
                  binding.MAnioN.setText(it.get("Anio_nac") as String?)
                  binding.MSexo.setText(it.get("Sexo") as String?)
                  binding.MTipoAve.setText(it.get("Tipo") as String?)
              }
          }*/

      // Volver a la actividad del homeFragment
      /*val homeFragment = Intent(activity, PajarosActivity::class.java)
      startActivity(homeFragment)*/

    }else {
      Toast.makeText(requireActivity().applicationContext, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
    }
    }

    return view

    }

    // Destruir la actividad
    override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
    }
}