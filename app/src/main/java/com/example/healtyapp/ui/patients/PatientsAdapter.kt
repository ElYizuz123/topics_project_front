package com.example.healtyapp.ui.patients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Patient

class PatientsAdapter(private val onClick: (Patient) -> Unit) :
    ListAdapter<Patient, PatientsAdapter.VH>(DIFF) {


    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Patient>() {
            override fun areItemsTheSame(oldItem: Patient, newItem: Patient) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Patient, newItem: Patient) =
                oldItem == newItem
        }
    }

    class VH(view: View, val onClick: (Patient) -> Unit) :
        RecyclerView.ViewHolder(view) {

        private val tvName = view.findViewById<TextView>(R.id.tvName)
        private val tvMeta = view.findViewById<TextView>(R.id.tvMeta)

        fun bind(item: Patient) {
            tvName.text = "${item.nombre} ${item.apellido}"
            tvMeta.text = "Edad: ${item.edad ?: "-"} · Género: ${item.genero ?: "-"}"
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false)
        return VH(v, onClick)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
