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
        private val tvEjercicio = v.findViewById<TextView>(R.id.tvEjercicio)
        private val tvMetricas = v.findViewById<TextView>(R.id.tvMetricas)
        private val layoutInfoExtra = v.findViewById<View>(R.id.layoutInfoExtra)

        fun bind(a: Appointment) {
            tvFH.text = "${a.fecha} · ${a.hora}"
            tvMotivo.text = a.motivo
            tvMeta.text = "Tipo: ${a.tipo} · Estado: ${a.estado}"
            
            // Mostrar información adicional si existe
            val hasExtraInfo = a.tipo_ejercicio != null || a.imc != null
            layoutInfoExtra.visibility = if (hasExtraInfo) View.VISIBLE else View.GONE
            
            if (hasExtraInfo) {
                // Información de ejercicio
                if (a.tipo_ejercicio != null) {
                    val ejercicioInfo = buildString {
                        append("💪 ${a.tipo_ejercicio}")
                        a.intensidad?.let { append(" · $it") }
                        a.tiempo?.let { append(" · ${it}min") }
                        a.frecuencia_semanal?.let { append(" · ${it}x/sem") }
                    }
                    tvEjercicio.text = ejercicioInfo
                    tvEjercicio.visibility = View.VISIBLE
                } else {
                    tvEjercicio.visibility = View.GONE
                }
                
                // Métricas corporales
                if (a.imc != null || a.porcentaje_grasa_corporal != null) {
                    val metricasInfo = buildString {
                        append("📊 ")
                        a.imc?.let { append("IMC: $it") }
                        if (a.imc != null && a.porcentaje_grasa_corporal != null) append(" · ")
                        a.porcentaje_grasa_corporal?.let { append("Grasa: $it%") }
                    }
                    tvMetricas.text = metricasInfo
                    tvMetricas.visibility = View.VISIBLE
                } else {
                    tvMetricas.visibility = View.GONE
                }
            }
            
            itemView.setOnClickListener {
                onCitaClick(a)
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_cita, p, false), onCitaClick)

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
