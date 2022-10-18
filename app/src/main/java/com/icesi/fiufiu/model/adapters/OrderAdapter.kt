package com.icesi.fiufiu.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Order
import com.icesi.fiufiu.model.holders.OrderViewHolder

class OrderAdapter: RecyclerView.Adapter<OrderViewHolder>()  {

    private val orders = ArrayList<Order>()
    var showNoOrder: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.order_row,parent,false)
        val orderViewHolder = OrderViewHolder(view)
        return orderViewHolder
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bindOrder(order)
    }

    fun showOrders() {
        showNoOrder = false
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun clear() {
        val size = orders.size
        orders.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addOrder(order: Order){
        orders.add(order)
        notifyItemInserted(orders.size-1)
    }
}
