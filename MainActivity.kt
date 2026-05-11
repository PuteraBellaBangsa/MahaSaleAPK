
package com.example.mahasale

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mahasale.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val session = SessionManager(this)
        if (!session.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        loadFragment(HomeFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home   -> loadFragment(HomeFragment())
                R.id.nav_search -> loadFragment(SearchFragment())
                R.id.nav_jual   -> {
                    if (!session.hasAlamat()) {
                        startActivity(Intent(this, AlamatActivity::class.java))
                    } else {
                        loadFragment(JualFragment())
                    }
                }
                R.id.nav_inbox  -> loadFragment(InboxFragment())
                R.id.nav_profil -> loadFragment(ProfilFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun updateCartBadge() {
        val repo  = ProdukRepository(this)
        val count = repo.countKeranjang()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val badge = bottomNav.getOrCreateBadge(R.id.nav_home)
        if (count > 0) { badge.isVisible = true; badge.number = count }
        else badge.isVisible = false
    }
}
