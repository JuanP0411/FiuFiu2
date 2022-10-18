package com.icesi.fiufiu.model.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.icesi.fiufiu.consumer.ConsumerMainOverviewFragment
import com.icesi.fiufiu.R
import com.icesi.fiufiu.model.Service
import com.icesi.fiufiu.util.Util

class MarketViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    //STATE
    var service: Service? = null
    lateinit var onSellerObserver: ConsumerMainOverviewFragment.SellerObserver

    //UI controllers
    var marketName: TextView = itemView.findViewById(R.id.marketNameRowTextView)
    var marketImageRow: ImageView = itemView.findViewById(R.id.marketRowImage)
    var descriptMarket: TextView = itemView.findViewById(R.id.descriptMarket)

    /**
     * Envia en patron observer el nombre y la descripcion
     */
    init {

        marketImageRow.setOnClickListener {
            var marketE = service!!
            onSellerObserver.sendMarket(marketE)
        }
    }

    fun bindMarket(serviceBind: Service) {
        service = serviceBind
        marketName.text = service!!.marketName
        descriptMarket.text = service!!.marketShortDescription
        Util.loadImage(service!!.imageID.toString(), marketImageRow, "market-image-profile")
    }
}
