package com.icesi.fiufiu.model.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Product
import com.icesi.fiufiu.seller.SellerMainOverviewFragment
import com.icesi.fiufiu.util.Util

class ProductSellerViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var product: Product? = null
    lateinit var onProductSellerObserver : SellerMainOverviewFragment.OnProductsOnSellerObserver

    //UI controllers
    var producImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var productNameRow: TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var productPriceRow: TextView = itemView.findViewById(R.id.descriptMarket)

    init {
        producImageRow.setOnClickListener {
            onProductSellerObserver.sendProduct(product!!)
        }
    }

    fun bindProduct(product: Product){
        this.product = product
        productNameRow.text = product.name
        productPriceRow.text = product.price.toString()
        Util.loadImage(product.imageID.toString(),producImageRow,"product-images" )
    }
}