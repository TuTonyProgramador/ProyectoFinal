package com.example.proyectofinal

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import com.example.proyectofinal.login.MainActivity
import com.example.proyectofinal.databinding.ActivityPajarosBinding
import com.example.proyectofinal.menu.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PajarosActivity : AppCompatActivity() {
    // Declarar variables privadas y lateinit
    private lateinit var toogle: ActionBarDrawerToggle
    lateinit var binding: ActivityPajarosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout de la actividad
        binding = ActivityPajarosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toogle = ActionBarDrawerToggle(this, binding.drawer, R.string.open_drawer, R.string.close_drawer)
        binding.drawer.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Cargar el correo en la cabecera del menu
        val nav = binding.navView.getHeaderView(0)
        val email = nav.findViewById<TextView>(R.id.CorreoUsu)
        email.text =  FirebaseAuth.getInstance().currentUser?.email
        obtenerDatos { userName ->
        val nombre = nav.findViewById<TextView>(R.id.Nombre)
        nombre.text = userName }

        // Metodo para obtener permisos
        obtenerPermisos()

        // Establecer un listener al menú de navegación
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.pajaros_registrados -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, HomeFragment())
                        commit()
                    }
                }
                R.id.anadir_nuevo -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, AnadirFragment())
                        commit()
                    }
                }
                R.id.anadir_criador -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, AnadirCriadores())
                        commit()
                    }
                }
                R.id.eliminar_criador -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, EliminarCriadores())
                        commit()
                    }
                }
                R.id.consultar_criadores -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, CriadorFragment())
                        commit()
                    }
                }
                R.id.modificar_ave -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, ModificarFragment())
                        commit()
                    }
                }
                R.id.calendario -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, CalendarioFragment())
                        commit()
                    }
                }
                R.id.modo_Oscuro -> {

                }
                R.id.soporte -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView, SoporteFragment())
                        commit()
                    }
                }
                R.id.cerrar_sesion -> {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this, "Ha cerrado seccion", Toast.LENGTH_SHORT).show()
                    // Volver a la actividad del MainActivity (al inicio de seccion)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                }
            }
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }

        // Modo oscuro
        // Obtener la referencia al botón switch del menú
        val switchDarkmode = binding.navView.menu.findItem(R.id.modo_Oscuro).actionView as Switch

        // Para marcar el switch
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkmode.isChecked = true
        } else {
            switchDarkmode.isChecked = false
        }

        // Agregar un listener al botón switch
        switchDarkmode.setOnCheckedChangeListener { _, isSelected ->
            if (isSelected) {
                // Activar modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // Aplicar el modo
                delegate.applyDayNight()
            } else {
                // Activar modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                // Aplicar el modo
                delegate.applyDayNight()
            }
        }

    }

    fun obtenerPermisos () {
        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        var db = FirebaseFirestore.getInstance()

        db.collection("Usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val privilegios = documentSnapshot.getString("Rol")

                    if (privilegios == "Usuario") {
                        // Menu
                        binding.navView.menu.findItem(R.id.anadir_criador).isVisible = false
                        binding.navView.menu.findItem(R.id.eliminar_criador).isVisible = false

                    }
                    if (privilegios == "Admin") {
                        binding.navView.menu.findItem(R.id.anadir_criador).isVisible = true
                        binding.navView.menu.findItem(R.id.eliminar_criador).isVisible = true
                    }
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
            }
    }

    fun obtenerDatos(onUserLoaded: (userName: String) -> Unit) {
        var nombre: String
        var apellidos: String
        var userName: String

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        auth.currentUser?.let {
            db.collection("Usuarios")
                .get()
                .addOnSuccessListener { documents ->
                    for (usuario in documents) {
                        if (usuario.id == auth.currentUser?.email){
                            nombre = usuario.getString("Nombre") ?: ""
                            apellidos = usuario.getString("Apellidos") ?: ""
                            userName = nombre + " " + apellidos
                            onUserLoaded(userName) // Llama a la función lambda con el nombre de usuario
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Usuario", "Error al obtener el usuario", exception)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
