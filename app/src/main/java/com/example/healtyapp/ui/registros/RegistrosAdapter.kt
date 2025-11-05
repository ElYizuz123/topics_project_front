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
        val tvObs = v.findViewById<TextView>(R.id.tvObs)

        fun bind(r: Registro) {
            tvFecha.text = r.fecha
            chkCumplio.isChecked = r.cumplio
            tvObs.text = r.observaciones ?: ""
        }
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) =
        VH(LayoutInflater.from(p.context).inflate(R.layout.item_registro, p, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
}
