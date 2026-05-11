package com.example.mahasale

import android.content.ContentValues
import android.content.Context
import com.example.mahasale.ProdukDatabaseHelper as DBH

class ProdukRepository(context: Context) {

    private val db = DBH(context)

    private fun cursorToProduk(c: android.database.Cursor) = Produk(
        id        = c.getInt(c.getColumnIndexOrThrow(DBH.COL_ID)),
        nama      = c.getString(c.getColumnIndexOrThrow(DBH.COL_NAMA)),
        kategori  = c.getString(c.getColumnIndexOrThrow(DBH.COL_KATEGORI)),
        harga     = c.getString(c.getColumnIndexOrThrow(DBH.COL_HARGA)),
        kondisi   = c.getString(c.getColumnIndexOrThrow(DBH.COL_KONDISI)),
        deskripsi = c.getString(c.getColumnIndexOrThrow(DBH.COL_DESKRIPSI)) ?: "",
        kontak    = c.getString(c.getColumnIndexOrThrow(DBH.COL_KONTAK)) ?: "",
        penjual   = c.getString(c.getColumnIndexOrThrow(DBH.COL_PENJUAL)) ?: "",
        rating    = c.getFloat(c.getColumnIndexOrThrow(DBH.COL_RATING))
    )

    // ── PRODUK ──────────────────────────────────────────────
    fun addProduk(produk: Produk): Long {
        val cv = ContentValues().apply {
            put(DBH.COL_NAMA, produk.nama);         put(DBH.COL_KATEGORI, produk.kategori)
            put(DBH.COL_HARGA, produk.harga);       put(DBH.COL_KONDISI, produk.kondisi)
            put(DBH.COL_DESKRIPSI, produk.deskripsi); put(DBH.COL_KONTAK, produk.kontak)
            put(DBH.COL_PENJUAL, produk.penjual);   put(DBH.COL_RATING, produk.rating)
        }
        return db.writableDatabase.insert(DBH.TABLE_PRODUK, null, cv)
    }

    fun getAllProduk(): List<Produk> {
        val list = mutableListOf<Produk>()
        val c = db.readableDatabase.query(
            DBH.TABLE_PRODUK, null, null, null, null, null, "${DBH.COL_ID} DESC"
        )
        c.use { while (it.moveToNext()) list.add(cursorToProduk(it)) }
        return list
    }

    fun searchProduk(keyword: String): List<Produk> {
        val list = mutableListOf<Produk>()
        val q = "%$keyword%"
        val c = db.readableDatabase.rawQuery(
            "SELECT * FROM ${DBH.TABLE_PRODUK} WHERE ${DBH.COL_NAMA} LIKE ? OR ${DBH.COL_KATEGORI} LIKE ? ORDER BY ${DBH.COL_ID} DESC",
            arrayOf(q, q)
        )
        c.use { while (it.moveToNext()) list.add(cursorToProduk(it)) }
        return list
    }

    fun getProdukByKategori(kategori: String): List<Produk> {
        if (kategori == "Semua") return getAllProduk()
        val list = mutableListOf<Produk>()
        val c = db.readableDatabase.query(
            DBH.TABLE_PRODUK, null,
            "${DBH.COL_KATEGORI} = ?", arrayOf(kategori),
            null, null, "${DBH.COL_ID} DESC"
        )
        c.use { while (it.moveToNext()) list.add(cursorToProduk(it)) }
        return list
    }

    fun getProdukById(id: Int): Produk? {
        val c = db.readableDatabase.query(
            DBH.TABLE_PRODUK, null,
            "${DBH.COL_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        return c.use { if (it.moveToFirst()) cursorToProduk(it) else null }
    }

    // ── KERANJANG ────────────────────────────────────────────
    fun addKeranjang(produk: Produk): Long {
        val cv = ContentValues().apply {
            put(DBH.COL_K_PRODUK_ID, produk.id)
            put(DBH.COL_K_NAMA, produk.nama)
            put(DBH.COL_K_HARGA, produk.harga)
            put(DBH.COL_K_KATEGORI, produk.kategori)
        }
        return db.writableDatabase.insert(DBH.TABLE_KERANJANG, null, cv)
    }

    fun getKeranjang(): List<Produk> {
        val list = mutableListOf<Produk>()
        val c = db.readableDatabase.rawQuery(
            "SELECT * FROM ${DBH.TABLE_KERANJANG}", null
        )
        c.use {
            while (it.moveToNext()) {
                list.add(Produk(
                    id       = it.getInt(it.getColumnIndexOrThrow(DBH.COL_K_PRODUK_ID)),
                    nama     = it.getString(it.getColumnIndexOrThrow(DBH.COL_K_NAMA)),
                    harga    = it.getString(it.getColumnIndexOrThrow(DBH.COL_K_HARGA)),
                    kategori = it.getString(it.getColumnIndexOrThrow(DBH.COL_K_KATEGORI)),
                    kondisi  = ""
                ))
            }
        }
        return list
    }

    fun removeKeranjang(produkId: Int) {
        db.writableDatabase.delete(
            DBH.TABLE_KERANJANG,
            "${DBH.COL_K_PRODUK_ID} = ?", arrayOf(produkId.toString())
        )
    }

    fun countKeranjang(): Int {
        val c = db.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM ${DBH.TABLE_KERANJANG}", null
        )
        return c.use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

    fun isInKeranjang(produkId: Int): Boolean {
        val c = db.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM ${DBH.TABLE_KERANJANG} WHERE ${DBH.COL_K_PRODUK_ID} = ?",
            arrayOf(produkId.toString())
        )
        return c.use { it.moveToFirst() && it.getInt(0) > 0 }
    }

    // ── FAVORIT ──────────────────────────────────────────────
    fun addFavorit(produkId: Int) {
        if (isFavorit(produkId)) return
        val cv = ContentValues().apply { put(DBH.COL_F_PRODUK_ID, produkId) }
        db.writableDatabase.insert(DBH.TABLE_FAVORIT, null, cv)
    }

    fun removeFavorit(produkId: Int) {
        db.writableDatabase.delete(
            DBH.TABLE_FAVORIT,
            "${DBH.COL_F_PRODUK_ID} = ?", arrayOf(produkId.toString())
        )
    }

    fun isFavorit(produkId: Int): Boolean {
        val c = db.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM ${DBH.TABLE_FAVORIT} WHERE ${DBH.COL_F_PRODUK_ID} = ?",
            arrayOf(produkId.toString())
        )
        return c.use { it.moveToFirst() && it.getInt(0) > 0 }
    }

    fun getFavorit(): List<Produk> {
        val list = mutableListOf<Produk>()
        val c = db.readableDatabase.rawQuery(
            "SELECT p.* FROM ${DBH.TABLE_PRODUK} p INNER JOIN ${DBH.TABLE_FAVORIT} f ON p.${DBH.COL_ID} = f.${DBH.COL_F_PRODUK_ID}",
            null
        )
        c.use { while (it.moveToNext()) list.add(cursorToProduk(it)) }
        return list
    }
}
