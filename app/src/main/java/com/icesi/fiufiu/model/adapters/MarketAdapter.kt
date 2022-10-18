package com.icesi.fiufiu.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.consumer.ConsumerMainOverviewFragment
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Service
import com.icesi.fiufiu.model.holders.MarketViewHolder

class MarketAdapter: RecyclerView.Adapter<MarketViewHolder>() {

    private val services = ArrayList<Service>()
    lateinit var onSellerObserver: ConsumerMainOverviewFragment.SellerObserver

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.market_row,parent,false)
        val marketViewHolder = MarketViewHolder(view)
        marketViewHolder.onSellerObserver = onSellerObserver
        return marketViewHolder
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val market = services[position]
        holder.bindMarket(market)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun clear() {
        val size = services.size
        services.clear()
        notifyItemRangeRemoved(0,size)
    }

    fun addMarket(service: Service){
        services.add(service)
        notifyItemInserted(services.size-1)
    }
}