package com.example.desafio3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val urlBase = "https://66fb02128583ac93b40aa0f6.mockapi.io/api/recursos/"
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adaptador
    private lateinit var Bar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        fetchAndDisplayRecursos()
    }

    private fun setupUI() {
        recycler = findViewById(R.id.recyclerview)
        Bar = findViewById(R.id.bar)

        recycler.layoutManager = LinearLayoutManager(this)
        adapter = Adaptador(emptyList(), ::onEditRecurso, ::onDeleteRecurso)
        recycler.adapter = adapter

        val refreshButton: Button = findViewById(R.id.agregar)
        refreshButton.setOnClickListener {
            fetchAndDisplayRecursos()
        }

        val addButton: Button = findViewById(R.id.agregar)
        addButton.setOnClickListener {
            showCreateRecursoDialog()
        }
    }

    private fun fetchAndDisplayRecursos() {
        Bar.visibility = View.VISIBLE
        recycler.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                val recursos = service.getRecursos()
                adapter.updateRecursos(recursos)
                recycler.visibility = View.VISIBLE
                if (recursos.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No se encontraron recursos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                val message = when (e) {
                    is IOException -> "Error de red: verifica tu conexión a internet"
                    is HttpException -> "Error del servidor: ${e.code()}"
                    else -> "Error: ${e.message}"
                }
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            } finally {
                Bar.visibility = View.GONE
            }
        }
    }

    private fun showCreateRecursoDialog() {
        val dialog = Crear(this) { nuevoRecurso ->
            crearNuevoRecurso(nuevoRecurso)
        }
        dialog.show()
    }

    private fun onEditRecurso(recurso: Modelo) {
        val dialog = Crear(this, recurso) { updatedRecurso ->
            actualizarRecurso(updatedRecurso)
        }
        dialog.show()
    }

    private fun onDeleteRecurso(recurso: Modelo) {
        eliminarRecurso(recurso)
    }

    private fun crearNuevoRecurso(nuevoRecurso: Modelo) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                val recursoCreado = service.createRecurso(nuevoRecurso)
                Toast.makeText(this@MainActivity, "Recurso creado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarRecurso(recurso: Modelo) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                service.updateRecurso(recurso.id, recurso)
                Toast.makeText(this@MainActivity, "Recurso actualizado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarRecurso(recurso: Modelo) {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RecursoApiService::class.java)

        lifecycleScope.launch {
            try {
                service.deleteRecurso(recurso.id)
                Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                fetchAndDisplayRecursos()
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
