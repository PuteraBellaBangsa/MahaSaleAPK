package com.example.mahasale.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mahasale.*

class ProfilFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profil, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = SessionManager(requireContext())

        view.findViewById<TextView>(R.id.tvNamaProfil).text = session.getNama()

        view.findViewById<LinearLayout>(R.id.btnJualDariProfil).setOnClickListener {
            if (!session.hasAlamat()) {
                startActivity(Intent(requireContext(), AlamatActivity::class.java))
            } else {
                (activity as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, JualFragment()).commit()
            }
        }

        view.findViewById<LinearLayout>(R.id.menuFavorit).setOnClickListener {
            Toast.makeText(requireContext(), "Favorit belum diimplementasikan", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<LinearLayout>(R.id.menuSettings).setOnClickListener {
            showSettingsDialog(session)
        }

        view.findViewById<LinearLayout>(R.id.menuContact).setOnClickListener {
            Toast.makeText(requireContext(), "Menghubungi support...", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<LinearLayout>(R.id.menuLogout).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Yakin ingin keluar dari MahaSale?")
                .setPositiveButton("Ya, Keluar") { _, _ ->
                    session.clearSession()
                    startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun showSettingsDialog(session: SessionManager) {
        val dialogView = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }
        val etNama = EditText(requireContext()).apply {
            hint = "Nama akun"
            setText(session.getNama())
        }
        val etAlamat = EditText(requireContext()).apply {
            hint = "Alamat penjual"
            setText(session.getAlamat())
        }
        dialogView.addView(TextView(requireContext()).apply { text = "Nama akun"; textSize = 12f })
        dialogView.addView(etNama)
        dialogView.addView(TextView(requireContext()).apply { text = "Alamat"; textSize = 12f; setPadding(0,16,0,0) })
        dialogView.addView(etAlamat)

        AlertDialog.Builder(requireContext())
            .setTitle("Settings")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                Toast.makeText(requireContext(), "Pengaturan disimpan", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
