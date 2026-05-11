
package com.example.mahasale.fragment

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mahasale.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchFragment : Fragment() {

    private lateinit var repo: ProdukRepository
    private lateinit var adapter: ProdukAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = ProdukRepository(requireContext())

        val rv       = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvSearch)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val chipGrp  = view.findViewById<ChipGroup>(R.id.chipGroup)

        adapter = ProdukAdapter(emptyList()) { produk ->
            startActivity(Intent(requireContext(), DetailProdukActivity::class.java).apply {
                putExtra("produk_id", produk.id)
            })
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // Chip trending
        val tags = listOf("Sepatu", "Hoodie", "Tas", "Jeans", "Jaket", "Elektronik", "Buku")
        tags.forEach { tag ->
            val chip = Chip(requireContext()).apply {
                text = tag
                isClickable = true
                isCheckable = false
                setOnClickListener {
                    etSearch.setText(tag)
                    adapter.updateData(repo.searchProduk(tag))
                }
            }
            chipGrp.addView(chip)
        }

        // Search saat mengetik
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                adapter.updateData(
                    if (keyword.isEmpty()) emptyList()
                    else repo.searchProduk(keyword)
                )
            }
        })

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

        etSearch.requestFocus()
    }
}
