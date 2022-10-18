package com.icesi.fiufiu.seller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.icesi.fiufiu.databinding.FragmentSellerOrderOverviewBinding
import com.icesi.fiufiu.model.AuxOrder
import com.icesi.fiufiu.model.Service
import com.icesi.fiufiu.model.Order
import com.icesi.fiufiu.model.Seller
import com.icesi.fiufiu.model.adapters.SellerOrdersHistoryAdapter
import com.icesi.fiufiu.model.adapters.SellerOrdersToConfirmAdapter
import com.icesi.fiufiu.util.Util

class SellerOrderOverviewFragment : Fragment() {

    /// View
    private var _binding: FragmentSellerOrderOverviewBinding? = null
    private val binding get() = _binding!!

    /// Objects
    private lateinit var user: Seller
    private lateinit var service: Service

    /// Adapters
    val adapterOrder = SellerOrdersToConfirmAdapter()
    val adapterOrderHistory = SellerOrdersHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerOrderOverviewBinding.inflate(inflater, container, false)
        getMarket()

        initOrdersRecyclerView(binding.ordersToConfirmRecycler).adapter = adapterOrder
        initOrdersRecyclerView(binding.ordersHistoryRecycler).adapter = adapterOrderHistory
        return binding.root
    }

    fun setUser(user: Seller) {
        this.user = user
    }

    private fun initOrdersRecyclerView(recycler: RecyclerView): RecyclerView{
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return recycler
    }

    private fun getOrders(marketID: String, typeOrder: String, typeAdapter: Boolean) {
        adapterOrderHistory.clear()
        adapterOrder.clear()
        Firebase.firestore.collection("markets").document(marketID).collection(typeOrder)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { order ->
                for (doc in order.result!!) {
                    val orderID = doc.toObject(AuxOrder::class.java)
                    Firebase.firestore.collection("orders").document(orderID.idOrder).get()
                        .addOnSuccessListener { orderReceived ->
                            val order = orderReceived.toObject(Order::class.java)
                            when (typeAdapter) {
                                true -> adapterOrder.addOrder(order!!)
                                false -> adapterOrderHistory.addOrder(order!!)
                            }
                        }
                }
            }
    }

    fun reloadOrders(idOrder: String){
        Firebase.firestore.collection("orders").document(idOrder).get()
            .addOnSuccessListener { orderReceived ->
                val order = orderReceived.toObject(Order::class.java)
                adapterOrderHistory.addOrder(order!!)
            }
    }

    private fun getMarket() {
        adapterOrder.clear()
        Firebase.firestore.collection("markets").document(user.marketID).get()
            .addOnSuccessListener {
                val currentService = it.toObject(Service::class.java)
                service = currentService!!
                val marketName = currentService.marketName
                binding.marketNameTextView.text = marketName
                Util.loadImage(currentService.imageID, binding.marketProfileImage, "market-image-profile")
            }
        getOrders(user.marketID, "historyOrders", false)
        getOrders(user.marketID, "pendentOrders", true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SellerOrderOverviewFragment()
    }

    interface OnConfirmOrderListener{
        fun confirmOrder(orderID: String, idUser: String)
        fun cancelOrder(orderID: String, idUser: String)
        fun editOrder(currentOrder: Order)
        fun editOrderSuccessful(currentOrder: Order)
    }
}