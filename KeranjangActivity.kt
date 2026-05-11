package com.example.mahasale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KeranjangActivity : AppCompatActivity() {

    private lateinit var repo: ProdukRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)
        repo = ProdukRepository(this)

        findViewById<TextView>(R.id.btnBackKeranjang).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnMulaiBelanjaEmpty).setOnClickListener { finish() }

        loadKeranjang()

        findViewById<Button>(R.id.btnBayar).setOnClickListener {
            Toast.makeText(this, "Fitur pembayaran segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadKeranjang() {
        val list = repo.getKeranjang()
        val kosong = findViewById<LinearLayout>(R.id.layoutKosong)
        val isi    = findViewById<LinearLayout>(R.id.layoutIsiKeranjang)

        if (list.isEmpty()) {
            kosong.visibility = View.VISIBLE
            isi.visibility    = View.GONE
            return
        }
        kosong.visibility = View.GONE
        isi.visibility    = View.VISIBLE

        val rv = findViewById<RecyclerView>(R.id.rvKeranjang)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = KeranjangAdapter(list.toMutableList()) { produkId ->
            repo.removeKeranjang(produkId)
            loadKeranjang()
        }

        // Hitung total
        val total = list.sumOf { it.harga.replace(Regex("[^0-9]"), "").toLongOrNull() ?: 0L }
        findViewById<TextView>(R.id.tvTotal).text =
            "Rp ${String.format("%,d", total).replace(",", ".")}"
    }
}

class KeranjangAdapter(
    private val list: MutableList<Produk>,
    private val onHapus: (Int) -> Unit
) : RecyclerView.Adapter<KeranjangAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama     = v.findViewById<TextView>(R.id.tvNamaKeranjang)
        val tvKategori = v.findViewById<TextView>(R.id.tvKategoriKeranjang)
        val tvHarga    = v.findViewById<TextView>(R.id.tvHargaKeranjang)
        val btnHapus   = v.findViewById<TextView>(R.id.btnHapusKeranjang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) {
        val p = list[pos]
        h.tvNama.text     = p.nama
        h.tvKategori.text = p.kategori
        h.tvHarga.text    = p.harga
        h.btnHapus.setOnClickListener { onHapus(p.id) }
    }

    override fun getItemCount() = list.size
}
