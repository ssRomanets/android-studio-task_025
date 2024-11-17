package com.example.task_025

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this, null)
    var products: MutableList<Product> = mutableListOf()

    private lateinit var toolbarMain: Toolbar
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var listViewLV: ListView
    private lateinit var saveBTN: Button

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        setSupportActionBar(toolbarMain)
        title = "Потребительская корзина."

        saveBTN.setOnClickListener {
            val productName = productNameET.text.toString()
            val productWeight = productWeightET.text.toString()
            val productPrice = productPriceET.text.toString()

            if (productName != "" && productWeight != "" && productPrice != "")
            {
                db.addProduct(productName, productWeight, productPrice)

                val cursor = db.getInfo()
                if (cursor != null && cursor.moveToLast()) {
                    cursor.moveToLast()
                    val productName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                    val productWeight = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WEIGHT)).toDouble()
                    val productPrice = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PRICE)).toInt()

                    val product = Product(productName, productWeight, productPrice)
                    products.add(product)

                    val listAdapter = ProductListAdapter(this@MainActivity, products)
                    listViewLV.adapter = listAdapter
                    listAdapter.notifyDataSetChanged()
                }
                clearEditFields()
            }

        }
    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)
        productNameET = findViewById(R.id.productNameET)
        productWeightET = findViewById(R.id.productWeightET)
        productPriceET = findViewById(R.id.productPriceET)
        listViewLV = findViewById(R.id.listViewLV)
        saveBTN = findViewById(R.id.saveBTN)
    }

    private fun clearEditFields() {
        productNameET.text.clear()
        productWeightET.text.clear()
        productPriceET.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain->{
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}