package com.example.healtyapp.ui.registros

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.example.healtyapp.R
import com.example.healtyapp.data.remote.dto.Registro

class RegistrosAdapter : androidx.recyclerview.widget.ListAdapter<Registro, RegistrosAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Registro>() {
            override fun areItemsTheSame(o: Registro, n: Registro) = o.id == n.id
            override fun areContentsTheSame(o: Registro, n: Registro) = o == n
        }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvFecha = v.findViewById<TextView>(R.id.tvFecha)
        val chkCumplio = v.findViewById<CheckBox>(R.id.chkCumplio)
        val tvDolorDificultad = v.findViewById<TextView>(R.id.tvDolorDificultad)
        val tvObs = v.findViewById<TextView>(R.id.tvObs)

        fun bind(r: Registro) {
            tvFecha.text = r.fecha
            chkCumplio.isChecked = r.cumplio
            
            // Mostrar dolor y dificultad
            val infoExtra = buildString {
                r.dolor_ejecucion?.let { dolor ->
                    append("💢 Dolor: $dolor/10")
                }
                r.dificultad_percibida?.let { dificultad ->
                    if (isNotEmpty()) append(" • ")
                    append("📊 Dificultad: ${dificultad.capitalize()}")
                }
            }
            
            if (infoExtra.isNotEmpty()) {
                tvDolorDificultad.text = infoExtra
                tvDolorDificultad.visibility = View.VISIBLE
            } else {
                tvDolorDificultad.visibility = View.GONE
            }
            
            if (r.observaciones.isNullOrEmpty()) {
                tvObs.visibility = View.GONE
            } else {
                tvObs.text = r.observaciones
                tvObs.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_registro, p, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
