package com.example.task_025

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ProductListAdapter(private val context: Context, productList: MutableList<Product>):
    ArrayAdapter<Product>(context, R.layout.list_item, productList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = getItem(position)
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val productIdText = view?.findViewById<TextView>(R.id.productIdTV)
        val productNameText = view?.findViewById<TextView>(R.id.productNameTV)
        val productWeightText = view?.findViewById<TextView>(R.id.productWeightTV)
        val productPriceText = view?.findViewById<TextView>(R.id.productPriceTV)
        productIdText?.text = "Id: ${product?.productId}"
        productNameText?.text = "Name: ${product?.productName}"
        productWeightText?.text = "Weight: ${product?.productWeight}"
        productPriceText?.text = "Price: ${product?.productPrice}"

        return view!!
    }
}