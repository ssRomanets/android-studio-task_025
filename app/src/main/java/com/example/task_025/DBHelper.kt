package com.example.task_025

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private val DATABASE_NAME = "PRODUCT_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME="product_table"
        val KEY_ID="id"
        val KEY_NAME="name"
        val KEY_WEIGHT="weight"
        val KEY_PRICE="price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("  +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT," +
                KEY_WEIGHT + " TEXT," +
                KEY_PRICE + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight.toString())
        contentValues.put(KEY_PRICE, product.productPrice.toString())
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range")
    fun readProducts(): MutableList<Product>{
        val productsList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return  productsList
        }
        var productId: Int
        var productName: String
        var productWeight: Double
        var productPrice: Int
        if (cursor.moveToFirst()) {
            do {
                productId = cursor.getInt(cursor.getColumnIndex("id"))
                productName = cursor.getString(cursor.getColumnIndex("name"))
                productWeight = cursor.getString(cursor.getColumnIndex("weight")).toDouble()
                productPrice = cursor.getString(cursor.getColumnIndex("price")).toInt()

                val product = Product(
                    productId = productId, productName = productName,
                    productWeight = productWeight, productPrice = productPrice
                )
                productsList.add(product)
            } while(cursor.moveToNext())
        }
        return  productsList
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight.toString())
        contentValues.put(KEY_PRICE, product.productPrice.toString())
        db.update(TABLE_NAME, contentValues, "id=" + product.productId, null)
        db.close()
    }

    fun daleteProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.productId)
        db.delete(TABLE_NAME, "id=" + product.productId, null)
        db.close()
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }
}