
package com.example.mahasale

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetailProdukActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        val repo     = ProdukRepository(this)
        val produkId = intent.getIntExtra("produk_id", -1)
        val produk   = repo.getProdukById(produkId) ?: run { finish(); return }

        findViewById<TextView>(R.id.tvNamaDetail).text    = produk.nama
        findViewById<TextView>(R.id.tvKategoriDetail).text = produk.kategori
        findViewById<TextView>(R.id.tvHargaDetail).text   = produk.harga
        findViewById<TextView>(R.id.tvKondisiDetail).text = produk.kondisi
        findViewById<TextView>(R.id.tvDeskripsiDetail).text = produk.deskripsi
        findViewById<TextView>(R.id.tvPenjualDetail).text = produk.penjual
        findViewById<TextView>(R.id.tvRatingDetail).text  = "★ ${produk.rating}"

        val btnKeranjang = findViewById<Button>(R.id.btnTambahKeranjang)
        val isMasuk = repo.isInKeranjang(produkId)
        btnKeranjang.text = if (isMasuk) "Sudah di keranjang" else "+ Masukkan keranjang"

        btnKeranjang.setOnClickListener {
            if (!repo.isInKeranjang(produkId)) {
                repo.addKeranjang(produk)
                btnKeranjang.text = "Sudah di keranjang"
                Toast.makeText(this, "Ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, KeranjangActivity::class.java))
            }
        }

        val btnFavorit = findViewById<Button>(R.id.btnFavorit)
        btnFavorit.text = if (repo.isFavorit(produkId)) "❤️ Favorit" else "🤍 Favorit"
        btnFavorit.setOnClickListener {
            if (repo.isFavorit(produkId)) {
                repo.removeFavorit(produkId)
                btnFavorit.text = "🤍 Favorit"
            } else {
                repo.addFavorit(produkId)
                btnFavorit.text = "❤️ Favorit"
            }
        }

        findViewById<Button>(R.id.btnHubungi).setOnClickListener {
            val pesan = "Halo, saya tertarik dengan *${produk.nama}* seharga ${produk.harga}. Masih tersedia?"
            val nomor = produk.kontak.replace(Regex("[^0-9]"), "")
            val wa    = "https://wa.me/62${nomor.removePrefix("0")}?text=${Uri.encode(pesan)}"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(wa)))
        }

        findViewById<TextView>(R.id.btnBackDetail).setOnClickListener { finish() }
    }
}
