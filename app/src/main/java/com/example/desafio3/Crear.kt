package com.example.desafio3

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Crear(context: Context, private val recurso: Modelo? = null, private val onSave: (Modelo) -> Unit) : Dialog(context) {
    private lateinit var Titulo: EditText
    private lateinit var Descripcion: EditText
    private lateinit var Tipo: EditText
    private lateinit var Enlace: EditText
    private lateinit var Imagen: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    init {
        setContentView(R.layout.adaptador)
        setupUI()
        recurso?.let { populateFields(it) }
    }

    private fun setupUI() {
        Titulo = findViewById(R.id.titulo)
        Descripcion = findViewById(R.id.descripcion)
        Tipo = findViewById(R.id.tipo)
        Enlace = findViewById(R.id.enlace)
        Imagen = findViewById(R.id.imagen)
        btnGuardar = findViewById(R.id.btnguardar)
        btnCancelar = findViewById(R.id.btncancelar)

        btnGuardar.setOnClickListener {
            if (validateFields()) {
                val nuevoRecurso = Modelo(
                    id = recurso?.id ?: 0,
                    titulo = Titulo.text.toString(),
                    descripcion = Descripcion.text.toString(),
                    tipo = Tipo.text.toString(),
                    enlace = Enlace.text.toString(),
                    imagen = Imagen.text.toString()
                )
                onSave(nuevoRecurso)
                dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }

    private fun populateFields(recurso: Modelo) {
        Titulo.setText(recurso.titulo)
        Descripcion.setText(recurso.descripcion)
        Tipo.setText(recurso.tipo)
        Enlace.setText(recurso.enlace)
        Imagen.setText(recurso.imagen)
    }

    private fun validateFields(): Boolean {
        if (Titulo.text.isEmpty()) {
            Toast.makeText(context, "Por favor ingresa un título", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
