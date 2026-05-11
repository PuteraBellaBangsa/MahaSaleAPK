
package com.example.mahasale

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdukAdapter(
    private var list: List<Produk>,
    private val onClick: (Produk) -> Unit
) : RecyclerView.Adapter<ProdukAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama     = v.findViewById<TextView>(R.id.tvNamaProduk)
        val tvKategori = v.findViewById<TextView>(R.id.tvKategori)
        val tvHarga    = v.findViewById<TextView>(R.id.tvHarga)
        val tvKondisi  = v.findViewById<TextView>(R.id.tvKondisi)
        val tvPenjual  = v.findViewById<TextView>(R.id.tvPenjual)
        val tvRating   = v.findViewById<TextView>(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val p = list[pos]
        h.tvNama.text     = p.nama
        h.tvKategori.text = p.kategori
        h.tvHarga.text    = p.harga
        h.tvKondisi.text  = p.kondisi
        h.tvPenjual.text  = p.penjual
        h.tvRating.text   = "★ ${p.rating}"
        h.itemView.setOnClickListener { onClick(p) }
    }

    override fun getItemCount() = list.size
    fun updateData(data: List<Produk>) { list = data; notifyDataSetChanged() }
}
