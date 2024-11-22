package com.example.task_025

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    var products: MutableList<Product> = mutableListOf()
    var listAdapter: ProductListAdapter? = null
    val dataBase = DBHelper(this)

    private lateinit var toolbarMain: Toolbar
    private lateinit var productIdET: EditText
    private lateinit var productNameET: EditText
    private lateinit var productWeightET: EditText
    private lateinit var productPriceET: EditText
    private lateinit var listViewLV: ListView

    private lateinit var saveBTN: Button
    private lateinit var updateBTN: Button
    private lateinit var deleteBTN: Button

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        setSupportActionBar(toolbarMain)
        title = "Потребительская корзина."

        saveBTN.setOnClickListener{
            saveRecord()
        }
    }

    override fun onResume() {
        super.onResume()
        updateBTN.setOnClickListener{
            updateRecord()
        }
        deleteBTN.setOnClickListener{
            deleteRecord()
        }
    }

    private fun init() {
        listViewLV = findViewById(R.id.listViewLV)
        toolbarMain = findViewById(R.id.toolbarMain)
        productIdET = findViewById(R.id.productIdET)
        productNameET = findViewById(R.id.productNameET)
        productWeightET = findViewById(R.id.productWeightET)
        productPriceET = findViewById(R.id.productPriceET)

        saveBTN = findViewById(R.id.saveBTN)
        updateBTN = findViewById(R.id.updateBTN)
        deleteBTN = findViewById(R.id.deleteBTN)
        viewDataAdapter()
    }

    private fun saveRecord() {
        val id = productIdET.text.toString()
        val name = productNameET.text.toString()
        val weight = productWeightET.text.toString()
        val price = productPriceET.text.toString()
        if (id.trim() != "" && name.trim() != "" && weight.trim() != "" && price.trim()!= "") {
            productIdET.setText("")
            productNameET.setText("")
            productWeightET.setText("")
            productPriceET.setText("")
            val product = Product(Integer.parseInt(id), name, weight.toDouble(), price.toInt())
            dataBase.addProduct(product)
            Toast.makeText(applicationContext, "Запись добавлена", Toast.LENGTH_LONG).show()
            viewDataAdapter()
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun updateRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val editId = dialogView.findViewById<EditText>(R.id.updateIdET)
        val editName = dialogView.findViewById<EditText>(R.id.updateNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.updateWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.updatePriceET)

        dialogBuilder.setTitle("Обновить запись")
        dialogBuilder.setMessage("введите данные ниже:")
        dialogBuilder.setPositiveButton("Обновить"){_, _ ->
            val updateId = editId.text.toString()
            val updateName = editName.text.toString()
            val updateWeight = editWeight.text.toString()
            val updatePrice = editPrice.text.toString()
            if (updateId.trim() != "" && updateName.trim() != "" &&
                updateWeight.trim() != "" && updatePrice.trim() != "") {
                val product = Product(
                    Integer.parseInt(updateId), updateName.toString(), updateWeight.toDouble(), updatePrice.toInt()
                )
                dataBase.updateProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Данные обновлены", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена"){dialog, which ->}
        dialogBuilder.create().show()
    }

    @SuppressLint("MissingInflatedId")
    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteId = dialogView.findViewById<EditText>(R.id.deleteIdET)
        dialogBuilder.setTitle("Удалить запись")
        dialogBuilder.setMessage("Введите идентификатор:")
        dialogBuilder.setPositiveButton("Удалить"){_, _,->
            val deletedId = chooseDeleteId.text.toString()
            if (deletedId.trim() != "") {
                val product = Product(Integer.parseInt(deletedId), "", 0.0, 0)
                dataBase.daleteProduct(product)
                viewDataAdapter()
                Toast.makeText(applicationContext, "Запись удалена", Toast.LENGTH_LONG).show()
            }
        }
        dialogBuilder.setNegativeButton("Отмена"){dialog, which ->}
        dialogBuilder.create().show()
    }

    private fun viewDataAdapter() {
        products = dataBase.readProducts()
        listAdapter = ProductListAdapter(this, products)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
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