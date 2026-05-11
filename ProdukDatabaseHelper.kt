package com.example.mahasale

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProdukDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME    = "mahasale.db"
        private const val DB_VERSION = 1
        const val TABLE_PRODUK  = "produk"
        const val COL_ID        = "id"
        const val COL_NAMA      = "nama"
        const val COL_KATEGORI  = "kategori"
        const val COL_HARGA     = "harga"
        const val COL_KONDISI   = "kondisi"
        const val COL_DESKRIPSI = "deskripsi"
        const val COL_KONTAK    = "kontak"
        const val COL_PENJUAL   = "penjual"
        const val COL_RATING    = "rating"

        const val TABLE_KERANJANG = "keranjang"
        const val COL_K_ID        = "id"
        const val COL_K_PRODUK_ID = "produk_id"
        const val COL_K_NAMA      = "nama"
        const val COL_K_HARGA     = "harga"
        const val COL_K_KATEGORI  = "kategori"

        const val TABLE_FAVORIT   = "favorit"
        const val COL_F_ID        = "id"
        const val COL_F_PRODUK_ID = "produk_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_PRODUK (
                $COL_ID        INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAMA      TEXT NOT NULL,
                $COL_KATEGORI  TEXT NOT NULL,
                $COL_HARGA     TEXT NOT NULL,
                $COL_KONDISI   TEXT NOT NULL DEFAULT 'Bekas',
                $COL_DESKRIPSI TEXT,
                $COL_KONTAK    TEXT,
                $COL_PENJUAL   TEXT,
                $COL_RATING    REAL DEFAULT 0
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_KERANJANG (
                $COL_K_ID        INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_K_PRODUK_ID INTEGER NOT NULL,
                $COL_K_NAMA      TEXT NOT NULL,
                $COL_K_HARGA     TEXT NOT NULL,
                $COL_K_KATEGORI  TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_FAVORIT (
                $COL_F_ID        INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_F_PRODUK_ID INTEGER NOT NULL
            )
        """.trimIndent())

        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUK")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_KERANJANG")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORIT")
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase) {
        val samples = listOf(
            arrayOf("Jaket Denim Levis",    "Jaket",    "Rp 150.000",   "Bekas", "Ukuran M kondisi 80%.",            "08123456789", "Runariys",   "4.8"),
            arrayOf("iPhone 11 64GB",        "Elektronik","Rp 3.500.000","Bekas", "Baterai 85%, mulus.",              "08234567890", "feelitstill","4.5"),
            arrayOf("Tas Ransel Eiger",      "Tas",      "Rp 200.000",   "Bekas", "25L, ritsleting normal.",          "08345678901", "vintageid",  "4.9"),
            arrayOf("Sepatu Nike Air Max",   "Sepatu",   "Rp 450.000",   "Bekas", "Size 42, sol bagus.",              "08456789012", "Runariys",   "4.7"),
            arrayOf("Hoodie Oversize Abu",   "Hoodie",   "Rp 120.000",   "Bekas", "Bahan fleece tebal, size L.",      "08567890123", "vintageid",  "4.6"),
            arrayOf("Kamera Canon 200D",     "Elektronik","Rp 4.000.000","Bekas", "Body only, sensor bersih.",        "08567890123", "feelitstill","4.8"),
            arrayOf("Celana Jeans Slim",     "Jeans",    "Rp 95.000",    "Bekas", "Size 30, kondisi 85%.",            "08678901234", "Runariys",   "4.5"),
            arrayOf("Headphone Sony WH1000", "Elektronik","Rp 800.000",  "Bekas", "Noise cancelling, ada dus.",       "08890123456", "feelitstill","4.9"),
            arrayOf("Kemeja Flannel Merah",  "Kemeja",   "Rp 75.000",    "Bekas", "Ukuran L, motif kotak.",           "08789012345", "vintageid",  "4.4"),
            arrayOf("Buku Novel Tere Liye",  "Buku",     "Rp 30.000",    "Bekas", "Kondisi baik, tidak ada coretan.", "08678901234", "Runariys",   "4.3")
        )
        samples.forEach { s ->
            val cv = ContentValues().apply {
                put(COL_NAMA, s[0]);      put(COL_KATEGORI, s[1])
                put(COL_HARGA, s[2]);     put(COL_KONDISI, s[3])
                put(COL_DESKRIPSI, s[4]); put(COL_KONTAK, s[5])
                put(COL_PENJUAL, s[6]);   put(COL_RATING, s[7].toFloat())
            }
            db.insert(TABLE_PRODUK, null, cv)
        }
    }
}
