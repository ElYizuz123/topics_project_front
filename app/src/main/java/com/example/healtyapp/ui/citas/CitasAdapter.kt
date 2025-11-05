package com.example.healtyapp.ui.citas

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Appointment

class CitasAdapter(private val onCitaClick: (Appointment) -> Unit) : ListAdapter<Appointment, CitasAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Appointment>() {
            override fun areItemsTheSame(o: Appointment, n: Appointment) = o.id == n.id
            override fun areContentsTheSame(o: Appointment, n: Appointment) = o == n
        }
    }

    class VH(v: View, private val onCitaClick: (Appointment) -> Unit) : RecyclerView.ViewHolder(v) {
        private val tvFH = v.findViewById<TextView>(R.id.tvFechaHora)
        private val tvMotivo = v.findViewById<TextView>(R.id.tvMotivo)
        private val tvMeta = v.findViewById<TextView>(R.id.tvMeta)

        fun bind(a: Appointment) {
            tvFH.text = "${a.fecha} · ${a.hora}"
            tvMotivo.text = a.motivo
            tvMeta.text = "Tipo: ${a.tipo} · Estado: ${a.estado}"
            
            itemView.setOnClickListener {
                onCitaClick(a)
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_cita, p, false), onCitaClick)

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
