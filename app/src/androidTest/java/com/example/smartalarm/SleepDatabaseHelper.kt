package com.example.smartalarm

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SleepDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "sleep_db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE sleep_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                magnitude REAL,
                category TEXT,
                timestamp LONG
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS sleep_data")
        onCreate(db)
    }

    fun insertData(magnitude: Float, category: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("magnitude", magnitude)
            put("category", category)
            put("timestamp", System.currentTimeMillis())
        }
        db.insert("sleep_data", null, values)
        db.close()
    }

    fun getAllData(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM sleep_data", null)

        val dataList = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val mag = cursor.getFloat(1)
            val cat = cursor.getString(2)
            val time = cursor.getLong(3)

            dataList.add("Mag: $mag | Cat: $cat | Time: $time")
        }

        cursor.close()
        db.close()
        return dataList
    }
}

