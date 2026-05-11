package com.example.mahasale

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AlamatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alamat)
        val session = SessionManager(this)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)

        findViewById<Button>(R.id.btnTambahAlamat).setOnClickListener {
            val alamat = etAlamat.text.toString().trim()
            if (alamat.isEmpty()) {
                Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            session.saveAlamat(alamat)
            Toast.makeText(this, "Alamat berhasil disimpan!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<TextView>(R.id.btnNantiSaja).setOnClickListener { finish() }
    }
}
