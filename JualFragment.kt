JualFragment.kt

package com.example.mahasale.fragment

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mahasale.*

class JualFragment : Fragment() {

    private var kategoriDipilih = ""
    private var hargaDiisi = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_jual, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repo    = ProdukRepository(requireContext())
        val session = SessionManager(requireContext())

        view.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

        view.findViewById<TextView>(R.id.btnTutupTips).setOnClickListener {
            view.findViewById<LinearLayout>(R.id.layoutTips).visibility = View.GONE
        }

        // Pilih kategori
        view.findViewById<LinearLayout>(R.id.rowKategori).setOnClickListener {
            val list = arrayOf("Jaket","Hoodie","Sepatu","Tas","Jeans","Elektronik","Kemeja","Buku","Lainnya")
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Pilih Kategori")
                .setItems(list) { _, which ->
                    kategoriDipilih = list[which]
                    view.findViewById<TextView>(R.id.tvKategoriPilih).text = "$kategoriDipilih ›"
                }.show()
        }

        // Input harga
        view.findViewById<LinearLayout>(R.id.rowHarga).setOnClickListener {
            val et = EditText(requireContext()).apply {
                hint = "Masukkan harga (contoh: 150000)"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Harga")
                .setView(et)
                .setPositiveButton("OK") { _, _ ->
                    hargaDiisi = et.text.toString().trim()
                    val formatted = hargaDiisi.toLongOrNull()?.let {
                        "Rp ${String.format("%,d", it).replace(",",".")}"
                    } ?: hargaDiisi
                    view.findViewById<TextView>(R.id.tvHargaPilih).text = "$formatted ›"
                }.show()
        }

        // Upload
        view.findViewById<Button>(R.id.btnUpload).setOnClickListener {
            val judul     = view.findViewById<EditText>(R.id.etJudul).text.toString().trim()
            val deskripsi = view.findViewById<EditText>(R.id.etDeskripsi).text.toString().trim()

            if (judul.isEmpty() || kategoriDipilih.isEmpty() || hargaDiisi.isEmpty()) {
                Toast.makeText(requireContext(), "Judul, kategori, dan harga wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hargaFormatted = hargaDiisi.toLongOrNull()?.let {
                "Rp ${String.format("%,d", it).replace(",",".")}"
            } ?: "Rp $hargaDiisi"

            repo.addProduk(Produk(
                nama      = judul,
                kategori  = kategoriDipilih,
                harga     = hargaFormatted,
                kondisi   = "Bekas",
                deskripsi = deskripsi,
                kontak    = session.getEmail(),
                penjual   = session.getNama(),
                rating    = 0f
            ))

            Toast.makeText(requireContext(), "Produk berhasil diupload!", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment()).commit()
        }
    }
}
