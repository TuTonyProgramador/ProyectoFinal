package com.example.proyectofinal.menu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.proyectofinal.PajarosActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentSoporteBinding


class SoporteFragment : Fragment(R.layout.fragment_soporte) {
    // Definimos una propiedad para el binding
    private var _binding: FragmentSoporteBinding? = null
    // Usamos delegación de propiedades para crear una propiedad "binding" no nula
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el layout con el binding
        _binding = FragmentSoporteBinding.inflate(inflater, container, false)

        // Agregamos un escucha de clic al ImageView
        binding.imageEmail.setOnClickListener {
            // Llamamos a la función "composeEmail" para crear un correo electrónico
            composeEmail("antonioortizgranados@gmail.com", "Sugerencia para Soporte", "")
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, PajarosActivity::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        // Devolvemos la vista raíz del binding
        return binding.root
    }

    // Función que crea un correo electrónico con los parámetros especificados y lo abre en una aplicación de correo
    private fun composeEmail(recipient: String, subject: String, body: String) {
        // Creamos un Intent con la acción ACTION_SEND para enviar un correo electrónico
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        // Comprobamos si hay una aplicación de correo electrónico disponible para manejar el Intent
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            // Si hay una aplicación disponible, abrimos el Intent
            startActivity(intent)
        } else {
            // Si no hay ninguna aplicación disponible, mostramos un mensaje Toast con una advertencia
            Toast.makeText(requireActivity(), "La aplicacion de Gmail no se encuentra instalada en el dispositivo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberamos el binding cuando se destruye la vista
        _binding = null
    }
}
