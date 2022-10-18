package com.icesi.fiufiu.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.consumer.ConsumerMainOverviewFragment
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Product
import com.icesi.fiufiu.model.holders.ProductViewHolder

class ProductAdapter: RecyclerView.Adapter<ProductViewHolder>() {

    private val products = ArrayList<Product>()
    lateinit var onProductObserver: ConsumerMainOverviewFragment.SellerObserver

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.product_row,parent,false)
        val productViewHolder = ProductViewHolder(view)
        productViewHolder.onProductObserver = onProductObserver
        return productViewHolder
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productn = products[position]

        holder.bindProduct(productn)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun clear() {
        val size = products.size
        products.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addProduct(product: Product){
        products.add(product)
        notifyItemInserted(products.size-1)
    }
}