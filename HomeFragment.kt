
package com.example.mahasale.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mahasale.*

class HomeFragment : Fragment() {

    private lateinit var repo: ProdukRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = ProdukRepository(requireContext())
        val session = SessionManager(requireContext())

        view.findViewById<TextView>(R.id.tvWelcome).text = "Welcome ${session.getNama()}! 👋"

        setupSellerCards(view)
        setupHotItems(view)
        updateCartBadge(view)

        view.findViewById<View>(R.id.layoutSearch).setOnClickListener {
            (activity as MainActivity).let {
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, SearchFragment())
                    .commit()
            }
        }

        view.findViewById<View>(R.id.btnKeranjang).setOnClickListener {
            startActivity(Intent(requireContext(), KeranjangActivity::class.java))
        }
    }

    private fun setupSellerCards(view: View) {
        val layout = view.findViewById<LinearLayout>(R.id.layoutSeller)
        val sellers = listOf(
            Triple("Runariys", "★★★★★", "4.8"),
            Triple("feelitstill", "★★★★", "4.5"),
            Triple("vintageid", "★★★★★", "4.9")
        )
        layout.removeAllViews()
        sellers.forEach { (nama, bintang, _) ->
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(8, 8, 8, 8)
                layoutParams = LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(0, 0, 8, 0)
                }
            }
            val grid = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(104, 80)
                setBackgroundColor(0xFFEEEEEE.toInt())
            }
            val tvNama = TextView(requireContext()).apply {
                text = nama; textSize = 11f
                setTextColor(0xFF111111.toInt())
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = 4 }
            }
            val tvBintang = TextView(requireContext()).apply {
                text = bintang; textSize = 11f
                setTextColor(0xFFFFA000.toInt())
            }
            card.addView(grid)
            card.addView(tvNama)
            card.addView(tvBintang)
            layout.addView(card)
        }
    }

    private fun setupHotItems(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rvHotItems)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ProdukAdapter(repo.getAllProduk()) { produk ->
            val intent = Intent(requireContext(), DetailProdukActivity::class.java)
            intent.putExtra("produk_id", produk.id)
            startActivity(intent)
        }
        rv.adapter = adapter

        view.findViewById<TextView>(R.id.tvFilterKategori).setOnClickListener {
            val kategoriList = arrayOf("Semua","Jaket","Hoodie","Sepatu","Tas","Jeans","Elektronik","Buku")
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Pilih Kategori")
                .setItems(kategoriList) { _, which ->
                    val kat = kategoriList[which]
                    view.findViewById<TextView>(R.id.tvFilterKategori).text = "$kat ▾"
                    adapter.updateData(repo.getProdukByKategori(kat))
                }.show()
        }
    }

    private fun updateCartBadge(view: View) {
        val count = repo.countKeranjang()
        val badge = view.findViewById<TextView>(R.id.tvCartBadge)
        if (count > 0) {
            badge.visibility = View.VISIBLE
            badge.text = count.toString()
        } else {
            badge.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        view?.let { updateCartBadge(it) }
    }
}
