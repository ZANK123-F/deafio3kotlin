package com.example.desafio3

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adaptador(
    private var recursos: List<Modelo>,
    private val Editarbuton: (Modelo) -> Unit,
    private val Eliminarbuton: (Modelo) -> Unit
) : RecyclerView.Adapter<Adaptador.RecursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recursos, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.bind(recurso)
    }

    override fun getItemCount(): Int = recursos.size

    fun updateRecursos(nuevosRecursos: List<Modelo>) {
        recursos = nuevosRecursos
        notifyDataSetChanged()
    }

    inner class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo: TextView = itemView.findViewById(R.id.titulo)
        private val descripcion: TextView = itemView.findViewById(R.id.descripcion)
        private val imagen: ImageView = itemView.findViewById(R.id.imagen)
        private val tipo: TextView = itemView.findViewById(R.id.tipo)

        private val editarButton: Button = itemView.findViewById(R.id.btn_editar)
        private val eliminarButton: Button = itemView.findViewById(R.id.btn_eliminar)
        private val verMas: Button = itemView.findViewById(R.id.btn_ver_mas)

        fun bind(recurso: Modelo) {
            titulo.text = recurso.titulo
            descripcion.text = recurso.descripcion
            tipo.text = recurso.tipo

            Glide.with(itemView.context)
                .load(recurso.imagen)
                .into(imagen)

            editarButton.setOnClickListener {
                Editarbuton(recurso)
            }

            eliminarButton.setOnClickListener {
                Eliminarbuton(recurso)
            }

            verMas.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recurso.enlace))
                itemView.context.startActivity(intent)
            }
        }
    }
}
