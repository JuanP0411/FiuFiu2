package com.icesi.fiufiu.model.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.consumer.ConsumerMainOverviewFragment
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Product
import com.icesi.fiufiu.util.Util

class ProductViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var product: Product? = null
    lateinit var onProductObserver: ConsumerMainOverviewFragment.SellerObserver

    //UI controllers
    var producImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var productNameRow: TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var productPriceRow: TextView = itemView.findViewById(R.id.descriptMarket)

    init {
        producImageRow.setOnClickListener {
            onProductObserver.sendProduct(
                Product(
                    product!!.id,
                    productNameRow.text.toString(),
                    Integer.parseInt(productPriceRow.text.toString()),
                    product!!.description,
                    product!!.imageID,
                    product!!.amount)
            )
        }
    }

    fun bindProduct(product: Product){
        this.product = product
        productNameRow.text = product.name
        productPriceRow.text = product.price.toString()
        Util.loadImage(product.imageID.toString(),producImageRow, "product-images" )
    }
}